package com.sdsu.edu.cms.dataservice.controllers;


import com.sdsu.edu.cms.common.models.cms.Submission;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.dataservice.services.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class SubmissionServiceController {

    @Autowired
    SubmissionService submissionService;

    @PostMapping("/submissions/create")
    public ServiceResponse createSubmission(@RequestBody Submission submission){
        ServiceResponse response =
                submissionService.addSubmission(submission);

        return response;
    }

    @PostMapping("/submissions/update")
    public ServiceResponse updateSubmission(@RequestBody Submission submission){
        ServiceResponse response =
                submissionService.updateSubmission(submission);

        return response;
    }

    @PostMapping("/submissions/get")
    public ServiceResponse getSubmission(@RequestParam Map<String, String> params){
        String confId = params.get("cid");
        String sid = "";
        if(params.containsKey("sid")){
            sid = params.get("sid");
        }
        ServiceResponse response = submissionService.getSubmission(confId, sid);

        return response;
    }

    @PostMapping("/submissions/delete")
    public ServiceResponse deleteConference(@RequestParam Map<String, String> mp){
        String submissionId = mp.get("sid");
        return  submissionService.deleteConference(submissionId);

    }

    @PostMapping("/submissions/patch")
    public ServiceResponse patchConference(@RequestBody String sid, @RequestParam Map<String, Object> mp){

        return  submissionService.patchConference(sid, mp);

    }

    @PostMapping("/submissions/author/delete")
    public ServiceResponse deleteAuthor(@RequestParam Map<String, String> params){
        return submissionService.deleteAuthor(params.get("sid"), params.get("aid"));
    }

    @PostMapping("/submissions/files/delete")
    public ServiceResponse deleteFiles(@RequestParam Map<String, String> params){
        return submissionService.deleteFiles(params.get("sid"), Integer.parseInt(params.get("type_id")));
    }


}
