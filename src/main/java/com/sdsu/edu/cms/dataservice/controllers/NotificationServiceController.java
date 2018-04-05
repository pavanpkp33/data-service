package com.sdsu.edu.cms.dataservice.controllers;

import com.sdsu.edu.cms.common.models.notification.NotifyDBModel;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1", produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE})
public class NotificationServiceController {

    @PostMapping("/notifications")
    public ServiceResponse saveNotifications(@RequestBody NotifyDBModel payLoad){

        return null;
    }
}
