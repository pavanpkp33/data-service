package com.sdsu.edu.cms.dataservice.controllers;

import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.dataservice.services.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1")
public class AssignmentServiceController {

    @Autowired
    AssignmentService assignmentService;

    @PostMapping("/assignment/create")
    public ServiceResponse addAssignment(@RequestParam Map<String, Object> params){
        return assignmentService.addAssignment(params);
    }
}
