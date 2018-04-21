package com.sdsu.edu.cms.dataservice.services;


import com.sdsu.edu.cms.common.models.notification.NotifyDBModel;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.dataservice.exception.NotificationNotFoundException;
import com.sdsu.edu.cms.dataservice.repository.NotificationServiceRepo;
import com.sdsu.edu.cms.dataservice.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class NotifyService {

    @Autowired
    NotificationServiceRepo notificationServiceRepo;

    public ServiceResponse saveNotifications(List<NotifyDBModel>notifications){
        try {
            for (int i = 0; i < notifications.size(); i++) {
                notificationServiceRepo.save(Query.SAVE_NOTIFICATION, notifications.get(i).getArray());
            }
            return new ServiceResponse(Arrays.asList(true), "Notification saved");
        }catch (Exception e){
            e.printStackTrace();
            return new ServiceResponse(Arrays.asList(false), e.getMessage());
        }
    }

    public ServiceResponse updateNotifications(Map<String, String> map){

        String query = buildQuery(map.get("field"), map.get("value"), map.get("id"));
        int i = notificationServiceRepo.update(query, null);
        if(i != 1) throw new NotificationNotFoundException(map.get("id"));
        return new ServiceResponse(Arrays.asList(true), "Notification updated successfully.");

    }

    public String buildQuery(String field, String value, String id){
        String query = "UPDATE NOTIFICATIONS SET "+field+" = \'"+value+"\' WHERE notification_id = \'"+id+"\'";
        return query;
    }

    public ServiceResponse getNotifications(Map<String, String> map){
        String id = map.get("id");
        String confId = map.get("cid");
        String baseQuery = Query.GET_NOTIFICATION;
        ServiceResponse response = new ServiceResponse();
        if(id != null && confId != null){
            baseQuery += "WHERE receiver_email= ? AND cid = ? AND notification_type = \"app\" AND valid = 'Y' AND has_seen = 'N'";
            response.setData(notificationServiceRepo.findAll(baseQuery, id, confId));
            response.setMessage("Notifications queried successfully");

        }else if(id == null && confId != null){
            baseQuery += "WHERE cid = ?";
            List<Object> res = notificationServiceRepo.findAll(baseQuery, confId);
            response.setData(res);
            response.setMessage("Notifications queried successfully");
        }else{
            response.setData(notificationServiceRepo.findAll(baseQuery, null));
            response.setMessage("Notifications queried successfully");
        }
        return response;
    }


}
