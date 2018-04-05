package com.sdsu.edu.cms.dataservice.proxy;


import com.sdsu.edu.cms.common.models.notification.Notify;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "notification-service")
@RibbonClient(name = "notification-service")
public interface NotificationServiceProxy {

    @PostMapping(value = "/api/v1/notify", consumes = {APPLICATION_JSON_VALUE})
    @ResponseBody
    ServiceResponse notify(@RequestBody Notify payLoad);
}
