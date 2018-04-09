package com.sdsu.edu.cms.dataservice.services;

import com.sdsu.edu.cms.common.models.cms.Conference;
import com.sdsu.edu.cms.common.models.cms.Track;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.common.utils.Constants;
import com.sdsu.edu.cms.dataservice.repository.ConferenceServiceRepo;
import com.sdsu.edu.cms.dataservice.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
           conferenceServiceRepo.save(Query.ADD_ROLE, conference.getConference_id(), conference.getChair_id(), Constants.ROLE_CHAIR);
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
}
