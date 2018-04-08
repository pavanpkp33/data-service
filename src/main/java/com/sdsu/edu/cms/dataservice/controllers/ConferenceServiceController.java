package com.sdsu.edu.cms.dataservice.controllers;

import com.sdsu.edu.cms.common.models.cms.Conference;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.dataservice.services.ConferenceMgmtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ConferenceServiceController {

    @Autowired
    ConferenceMgmtService conferenceMgmtService;

    @PostMapping("/conferences/create")
    public ServiceResponse createConference(@RequestBody Conference conference){
        return conferenceMgmtService.createNewConference(conference);
    }
}
