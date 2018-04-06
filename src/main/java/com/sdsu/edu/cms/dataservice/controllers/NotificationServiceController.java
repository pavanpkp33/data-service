package com.sdsu.edu.cms.dataservice.controllers;

import com.sdsu.edu.cms.common.models.notification.NotifyDBModel;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.dataservice.services.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1", produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE})
public class NotificationServiceController {
    @Autowired
    NotifyService notifyService;

    @PostMapping("/notifications")
    public ServiceResponse saveNotifications(@RequestBody List<NotifyDBModel> payLoad){
        return notifyService.saveNotifications(payLoad);

    }

    @PostMapping("/notifications/update")
    public ServiceResponse updateNotifications(@RequestParam Map<String, String> map){
        return notifyService.updateNotifications(map);
    }
}
