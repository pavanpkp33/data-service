package com.sdsu.edu.cms.dataservice.services;

import com.google.gson.Gson;
import com.sdsu.edu.cms.common.models.cms.Conference;
import com.sdsu.edu.cms.common.models.cms.Track;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.common.utils.Constants;
import com.sdsu.edu.cms.dataservice.repository.ConferenceServiceRepo;
import com.sdsu.edu.cms.dataservice.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ConferenceMgmtService {

    @Autowired
    ConferenceServiceRepo conferenceServiceRepo;

    public ServiceResponse createNewConference(Conference conference){
       int i = conferenceServiceRepo.save(Query.CREATE_CONFERENCE, conference.getData());
       if(i == -1){
           return new ServiceResponse(Arrays.asList(false), "-1");
       }else if(i == -2){
           return  new ServiceResponse(Arrays.asList(false), "-2");
       }else{
           conferenceServiceRepo.save(Query.ADD_ROLE, conference.getCid(), conference.getChair_uid(), Constants.ROLE_CHAIR);
           return  new ServiceResponse(Arrays.asList(true), "Conference created successfully");
       }
    }


    public ServiceResponse addTracksForConference(List<Track> tracks, String id){

        tracks.forEach((track -> {
            List<Object[]> paramList = new ArrayList<>();
            int tId = conferenceServiceRepo.saveAndGetId(Query.ADD_TRACK, track.getTrack_name(), id);
            track.setTrackId(tId);
            for(String keyword : track.getKeywords()){
                Object[] arr = {tId, keyword};
                paramList.add(arr);
            }
            conferenceServiceRepo.batchInsert(Query.ADD_KEYWORDS_TRACK, paramList);
        }));

        return new ServiceResponse(Arrays.asList(true), "tracks inserted successfully.");

    }

    public ServiceResponse getConferenceByName(String name){
        Object response = conferenceServiceRepo.findById(Query.GET_CONF_BY_NAME, name);
        if(response.toString().equals("-1")){
            return new ServiceResponse(Arrays.asList("false"), "Conference with ID not found");
        }

        return new ServiceResponse(Arrays.asList(response), "Query successful.");


    }

    public ServiceResponse getTrackNames(String id){
        List<Track> tracks = conferenceServiceRepo.findTracksById(Query.GET_TRACKS, id);
        for(Track track : tracks){
            track.setKeywords(conferenceServiceRepo.findKeywordsForTracks(Query.GET_TRACKS_KW, track.getTrackId()));
        }
        String jsonStr = new Gson().toJson(tracks).toString();
        return new ServiceResponse(Arrays.asList(jsonStr), "Query successful.");
    }

    public ServiceResponse updateConferenceData(Conference conference, String id){
         String query = buildQuery(conference, id);
         int status = conferenceServiceRepo.save(query, null);
         if(status == 0 || status == -1) return new ServiceResponse(Arrays.asList(false), "-1");
         return  new ServiceResponse(Arrays.asList(true), "Conference updated successfully.");
    }

    public String buildQuery(Conference conference, String id){
        String query = "UPDATE CONFERENCE SET ";
        boolean flag = true;
        for (Field field : conference.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(conference);
                if(value != null && !value.toString().equals("0")){
                    if(field.getName().equals("start_date") || field.getName().equals("end_date")){
                        query += ", "+ field.getName() +" = \""+new SimpleDateFormat("yyyy-MM-dd").format(value)+ "\" "; continue;
                    }
                    if(flag){
                        query += field.getName() +" = \""+value.toString()+"\"";
                        flag=false;
                    }else{
                        query += ", "+ field.getName() +" = \""+value.toString()+ "\" ";
                    }

                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        query += "WHERE cid = \""+id+"\"";
        return query;
    }

    @Transactional
    public ServiceResponse deleteConference(String id){
        int i = conferenceServiceRepo.update(Query.DELETE_CONFERENCE, id);
        int j = conferenceServiceRepo.update(Query.DELETE_CONF_ROLES, id);

        if(i != -1 || j != -1){
            return new ServiceResponse(Arrays.asList(true), "Conference deleted Successfully");
        }else{
            return new ServiceResponse(Arrays.asList(false), "Failed to delete conference");
        }
    }

    public ServiceResponse getConferenceUsers(String confId) {

        return new ServiceResponse(conferenceServiceRepo.findAll(Query.GET_CONFERENCE_USERS, confId), "Users queried successfully");
    }

    public ServiceResponse deleteUserRole(String cid, String uid, String rid) {
        String query = "UPDATE conf_roles SET valid = 'N' WHERE uid = '"+uid+"' AND cid = '"+cid+"' AND role_id = '"+rid+"'";
        System.out.println(query);
        int i =conferenceServiceRepo.update(query,null);
        if(i != -1){
            return new ServiceResponse(Arrays.asList(true), "User role deleted successfully" );
        }else{
            return new ServiceResponse(Arrays.asList(false), "Failed to delete user roles" );
        }

    }
}
