package com.sdsu.edu.cms.dataservice.services;

import com.google.gson.Gson;
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



        return new ServiceResponse(null, null);
    }

}
