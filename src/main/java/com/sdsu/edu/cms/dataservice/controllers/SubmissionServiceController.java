package com.sdsu.edu.cms.dataservice.controllers;


import com.sdsu.edu.cms.common.models.cms.Submission;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.dataservice.services.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
