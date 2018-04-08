package com.sdsu.edu.cms.dataservice.services;

import com.sdsu.edu.cms.common.models.cms.Conference;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.dataservice.repository.ConferenceServiceRepo;
import com.sdsu.edu.cms.dataservice.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
           return  new ServiceResponse(Arrays.asList(true), "Conference created successfully");
       }
    }
}
