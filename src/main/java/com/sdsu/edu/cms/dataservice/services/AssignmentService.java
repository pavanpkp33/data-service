package com.sdsu.edu.cms.dataservice.services;

import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.dataservice.repository.AssignmentServiceRepo;
import com.sdsu.edu.cms.dataservice.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
public class AssignmentService {
    @Autowired
    AssignmentServiceRepo assignmentServiceRepo;

    @Transactional
    public ServiceResponse addAssignment(Map<String, Object> mp){
        String sid = mp.get("sid").toString();
        String cid = mp.get("cid").toString();
        String reviewer =  mp.get("userids").toString();
        String[] reviewers = reviewer.split("~");
        try{
            for(String id : reviewers){
                assignmentServiceRepo.save(Query.ASSIGN_REVIEWER, UUID.randomUUID().toString(), id, sid, cid);
            }
            return new ServiceResponse(Arrays.asList(true), "Reviewers added successfully");
        }catch (Exception e){
            return new ServiceResponse(Arrays.asList(e.getMessage()), "Failed to add reviewers");
        }

    }

    public ServiceResponse getAssignments(String sid) {
        return  new ServiceResponse(assignmentServiceRepo.findAll(Query.GET_ASSIGNMENT_BY_SID, sid),
                "Assignments queried successfully");

    }

    public ServiceResponse deleteAssignment(Map<String, Object> params) {
        String sid = params.get("sid").toString();


        try{
            assignmentServiceRepo.save(Query.DELETE_ASSIGNMENT_BY_SID, sid);
            return new ServiceResponse(Arrays.asList(true), "Reviewers deleted successfully");
        }catch (Exception e){
            return new ServiceResponse(Arrays.asList(e.getMessage()), "Failed to delete reviewers");
        }

    }
}
