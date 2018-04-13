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
}
