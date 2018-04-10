package com.sdsu.edu.cms.dataservice.services;

import com.sdsu.edu.cms.common.models.cms.Submission;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.dataservice.repository.SubmissionServiceRepo;
import com.sdsu.edu.cms.dataservice.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SubmissionService {

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


        return null;
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

}
