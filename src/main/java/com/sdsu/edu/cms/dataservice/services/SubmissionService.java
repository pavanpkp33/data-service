package com.sdsu.edu.cms.dataservice.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdsu.edu.cms.common.models.cms.Authors;
import com.sdsu.edu.cms.common.models.cms.Submission;
import com.sdsu.edu.cms.common.models.notification.Notify;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.common.models.user.AuthUser;
import com.sdsu.edu.cms.common.models.user.User;
import com.sdsu.edu.cms.common.utils.Constants;
import com.sdsu.edu.cms.common.utils.EmailTemplate;
import com.sdsu.edu.cms.dataservice.exception.UserNotFoundException;
import com.sdsu.edu.cms.dataservice.proxy.NotificationServiceProxy;
import com.sdsu.edu.cms.dataservice.repository.AuthServiceRepo;
import com.sdsu.edu.cms.dataservice.repository.SubmissionServiceRepo;
import com.sdsu.edu.cms.dataservice.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SubmissionService {

    @Autowired
    AuthServiceRepo authServiceRepo;

    @Autowired
    NotificationServiceProxy notificationServiceProxy;

    @Autowired
    SubmissionServiceRepo submissionServiceRepo;

    /*
           Steps for new submission handling.
           1. Insert in submission table.
           2. Update keywords table.
           3. check for users in user table
               a. if exists, insert into conf_users table
               b. if user doesnt exists in DB, create a default user and insert into conf_users
    */
    @Transactional
    public ServiceResponse addSubmission(Submission submission) throws RuntimeException{
       int trackId =  assignGroup(submission.getCid(), submission.getKeyword());
       submission.setGroup_app(trackId);
       submission.setSid(UUID.randomUUID().toString());
       submission.setLast_updated(new Date());
       submission.setSubmission_date(new Date());
       submissionServiceRepo.save(Query.ADD_SUBMISSION, submission.getParams());
       String[] kws = submission.getKeyword();
       for(String keyword : kws){
           submissionServiceRepo.save(Query.ADD_KEYWORDS_SUBMISSION, submission.getSid(), keyword);
       }
       List<Authors> authors = submission.getAuthorsList();
       List<String>  emailAuthors = new ArrayList<>();
       for(Authors a : authors){
           emailAuthors.add(a.getEmail());
           try{
               String res = authServiceRepo.findOne(Query.GET_USER_BY_EMAIL, a.getEmail()).toString();
               AuthUser u =  new Gson().fromJson(res, AuthUser.class);
               submissionServiceRepo.save(Query.ADD_ROLE, submission.getCid(), u.getId(), Constants.ROLE_AUTHOR);
               submissionServiceRepo.save(Query.ADD_CONF_SUB_USERS, submission.getCid(), submission.getSid(), u.getId(), a.getIs_corresponding());

           }catch (UserNotFoundException e){
               // Insert user into DB

               addUserWithDefaultValues(a, submission);


           }
       }
       EmailTemplate temp = new EmailTemplate();
       String url = "http://localhost:4200/conferences/"+submission.getCid()+"/"+submission.getSid();
       temp.setSubmissionTemplate(url, submission.getSid());
       sendNotifications( temp.getSubmissionTemplate(),"Important information about your paper submission.", emailAuthors );
       return new ServiceResponse(Arrays.asList(true), "Submission added successfully");
    }

    private void addUserWithDefaultValues(Authors a, Submission submission) {
        User user = new User();
        user.setvalid("Y");
        user.setis_active("Y");
        user.setis_participating("Y");
        user.setEmail(a.getEmail());
        user.setId(UUID.randomUUID().toString());
        user.setfirst_name(a.getFirst_name());
        user.setlast_name(a.getLast_name());
        user.setmiddle_name(a.getMiddle_name());
        user.setTitle(a.getTitle());
        user.setAffiliation(a.getAffiliation());
        user.setPassword(BCrypt.hashpw(a.getEmail(), BCrypt.gensalt()));
        submissionServiceRepo.save(Query.SAVE_USER, user.getArray());
        submissionServiceRepo.save(Query.ADD_ROLE, submission.getCid(), user.getId(), Constants.ROLE_AUTHOR);
        submissionServiceRepo.save(Query.ADD_CONF_SUB_USERS, submission.getCid(), submission.getSid(), user.getId(), a.getIs_corresponding());
        EmailTemplate template = new EmailTemplate();
        template.setUserTemplate(a.getEmail(), a.getEmail());
        sendNotifications(template.getUserTemplate(), Constants.NEW_USER_ACCOUNT, Arrays.asList(a.getEmail()));
        
    }
    @Async("notificationExecutor")
    public void sendNotifications(String template, String type, List<String> emails) {
        notificationServiceProxy.notify(buildPayLoad(template, type
                , emails));
    }



    public int assignGroup(String confId, String[] keywords){
        HashMap<Integer, HashSet<String>> map = (HashMap) submissionServiceRepo.findOne(Query.GET_TRACKS_KW_FOR_CID, confId);
        AtomicInteger tid = new AtomicInteger(0);
        AtomicInteger maxCount = new AtomicInteger(0);
        AtomicInteger count = new AtomicInteger(0);
        map.forEach((k, v)->{
            for(String s : keywords){
                if(v.contains(s)) count.getAndIncrement();
            }
            if(count.get() >  maxCount.get()){
                maxCount.set(count.get());
                tid.set(k);
            }
        });
        return tid.get();
    }

    private Notify buildPayLoad(String template, String subject, List<String> receivers) {


        Notify n = new Notify();
        n.setConference_id(Constants.SERVER_CONFERENCE);
        n.setCreated_on(new Date());
        n.setEmail_message(template);
        n.setIs_broadcast("N");
        n.setMethod(Arrays.asList(Constants.SCHEME_EMAIL));
        n.setPriority(Constants.NotifyMethod.MESSAGE.toString());
        n.setReceiver(receivers);
        n.setSender_id(Constants.SERVER_ID);
        n.setSubject(subject);
        n.setSender_name(Constants.SERVER_NAME);

        return n;
    }

    @Transactional
    public ServiceResponse updateSubmission(Submission submission) throws RuntimeException{
        String sid = submission.getSid();
        submission.setSid(null);
        //delete the keywords first.
        if(submission.getKeyword() != null && submission.getKeyword().length > 0){
            submissionServiceRepo.delete(Query.DELETE_USER_KEYWORDS, new Object[]{sid});
            for(String kw : submission.getKeyword()){
                submissionServiceRepo.save(Query.ADD_KEYWORDS_SUBMISSION, sid, kw);
            }
        }

        submissionServiceRepo.delete(Query.DELETE_FILES, new Object[]{sid});
        if(submission.getDraftPaperUri() != null && !submission.getDraftPaperUri().isEmpty()){
            submissionServiceRepo.save(Query.SAVE_FILES, UUID.randomUUID().toString(),1,submission.getDraftPaperUri(),
                    new Date(), submission.getSubmit_author_id(), "Y", sid);
        }
        if(submission.getFinalPaperUri() != null && !submission.getFinalPaperUri().isEmpty()){
            submissionServiceRepo.save(Query.SAVE_FILES, UUID.randomUUID().toString(),2,submission.getFinalPaperUri(),
                    new Date(), submission.getSubmit_author_id(), "Y", sid);
        }
        if(submission.getCameraReadyPaperUri() != null && !submission.getCameraReadyPaperUri().isEmpty()){
            submissionServiceRepo.save(Query.SAVE_FILES, UUID.randomUUID().toString(),3,submission.getCameraReadyPaperUri(),
                    new Date(), submission.getSubmit_author_id(), "Y", sid);
        }
        //Updating submission table.
        submissionServiceRepo.save(Query.UPDATE_SUBMISSION, submission.getTitle(), submission.getTrack_id(), submission.getAbstract_text(),
                new Date(), sid);


        return new ServiceResponse(Arrays.asList(true), "Submission updated successfully");
    }

    @Transactional
    public ServiceResponse getSubmission(String cid, String sid){
        List<Submission> result;
        Map<Integer, String> mp;
        if(sid == "" || sid == null ){

            result = submissionServiceRepo.findSubmisisons(Query.GET_SUBMISSION_BY_CONF, cid);
            if(result.isEmpty()) return new ServiceResponse(null, "No submissions found");

            for(Submission s : result){
                s.setKeyword(submissionServiceRepo.getKeywords(Query.GET_KWS_BY_SID, s.getSid()));
                s.setAuthorsList(submissionServiceRepo.getAuthors(Query.GET_AUTHORS_BY_SID,s.getSid()));
                mp = submissionServiceRepo.getFilesData(Query.GET_FILES_BY_SID, s.getSid());
                processFilesInput(s, mp);

            }
        }else{
            result = submissionServiceRepo.findSubmisisons(Query.GET_SUBMISSION_BY_ID, sid);
            if(result.isEmpty()) return new ServiceResponse(null, "No submissions found");
            result.get(0).setKeyword(submissionServiceRepo.getKeywords(Query.GET_KWS_BY_SID, sid));
            result.get(0).setAuthorsList(submissionServiceRepo.getAuthors(Query.GET_AUTHORS_BY_SID,sid));
            mp = submissionServiceRepo.getFilesData(Query.GET_FILES_BY_SID, sid);
            processFilesInput(result.get(0), mp);

        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Object t = gson.toJson(result).toString();

        return new ServiceResponse(Arrays.asList(t), "Query successful.");
    }

    private Submission processFilesInput(Submission s, Map<Integer, String> mapOfFiles){
        for(Integer i : mapOfFiles.keySet()){
            switch (i){
                case 1 : s.setDraftPaperUri(mapOfFiles.get(i));
                         break;


                case 2: s.setFinalPaperUri(mapOfFiles.get(i));
                        break;

                case 3: s.setCameraReadyPaperUri(mapOfFiles.get(i));

            }
        }

        return s;

    }

    @Transactional
    public ServiceResponse deleteConference(String sid){
        submissionServiceRepo.delete(Query.DELETE_SUBMISSION, sid);
        submissionServiceRepo.delete(Query.DELETE_CONF_USERS, sid);
        return new ServiceResponse(Arrays.asList(true), "Submission deleted successfully.");
    }

    @Transactional
    public  ServiceResponse patchConference(String sid, Map params){
        String query = buildPatchQuery(sid, params);
        submissionServiceRepo.save(query, null);
        return new ServiceResponse(Arrays.asList(true), "Submission updated successfully.");

    }

    private String buildPatchQuery(String sid, Map<String, Object> params) {
        String query = "UPDATE submissions SET ";
        boolean flag = true;
        for(String key : params.keySet()){
            if(key.equals("group_app") || key.equals("track_id")){
                if(flag){
                    query += key+" = "+params.get(key).toString();
                    flag= false;
                }else{
                    query += ", "+key+" = "+params.get(key).toString();
                }
            }else{
                if(flag){
                    query += key+" = '"+params.get(key)+"'";
                    flag= false;
                }else{
                    query += ", "+key+" = '"+params.get(key)+"'";
                }
            }
        }

        query += " WHERE sid = '"+sid+"'";
        return  query;
    }


    public ServiceResponse deleteAuthor(String sid, String aid) {
        submissionServiceRepo.save(Query.DELETE_AUTHOR_FOR_SUB, sid, aid);
        return new ServiceResponse(Arrays.asList(true), "Author deleted successfully");
    }

    public ServiceResponse deleteFiles(String sid, int type_id) {
        submissionServiceRepo.save(Query.DELETE_FILES_SINGLE, sid, type_id);
        return new ServiceResponse(Arrays.asList(true), "File deleted successfully");
    }

    public ServiceResponse getUserSubmissions(String cid, String uid) {

        List<Submission> result;
        Map<Integer, String> map;

        result = submissionServiceRepo.findSubmisisons(Query.GET_SUBMISSION_BY_UID, cid, uid);
        if(result.isEmpty()) return new ServiceResponse(null, "No submissions found");

        for(Submission s : result){
            s.setKeyword(submissionServiceRepo.getKeywords(Query.GET_KWS_BY_SID, s.getSid()));
            s.setAuthorsList(submissionServiceRepo.getAuthors(Query.GET_AUTHORS_BY_SID,s.getSid()));
            map = submissionServiceRepo.getFilesData(Query.GET_FILES_BY_SID, s.getSid());
            processFilesInput(s, map);

        }

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Object t = gson.toJson(result).toString();

        return new ServiceResponse(Arrays.asList(t), "Query successful.");

    }
}
