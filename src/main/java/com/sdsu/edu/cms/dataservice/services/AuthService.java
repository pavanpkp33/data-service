package com.sdsu.edu.cms.dataservice.services;


import com.sdsu.edu.cms.common.models.notification.Notify;
import com.sdsu.edu.cms.common.models.user.User;
import com.sdsu.edu.cms.dataservice.proxy.NotificationServiceProxy;
import com.sdsu.edu.cms.dataservice.repository.AuthServiceRepo;
import com.sdsu.edu.cms.dataservice.util.Query;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.sdsu.edu.cms.common.utils.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthService{
    @Autowired
    AuthServiceRepo authServiceRepo;

    @Autowired
    NotificationServiceProxy notificationServiceProxy;

    public Object authenticateUser(Object id){

        return authServiceRepo.findOne(Query.GET_USER_BY_EMAIL, id);

    }

    public int saveUser(User user){
          Notify notifyPayLoad;
          String activationToken = null, userId = user.getId();
          int res = authServiceRepo.save(Query.SAVE_USER, user.getArray());
          if(res != -1) {
              String uuid = UUID.randomUUID().toString();
              activationToken = generateActivationToken(uuid, userId);
              int c = authServiceRepo.save(Query.SAVE_ACTIVATION_TOKEN, uuid, userId, activationToken);
              if(c != -1){
                  /*
                  TODO:: can make it better by executing in a separate thread. Future enhancements.

                   */
                  try{
                      notifyPayLoad = buildPayLoad(user.getfirst_name(), user.getEmail(), userId, activationToken);
                      notifyAsync(notifyPayLoad);
                      return 1;
                  }catch (FeignException e){
                      System.out.println(e.getMessage());
                      return -1;
                  }


              }
          }
          return -1;

    }

    private Notify buildPayLoad(String name, String email, String userId, String activationToken) {

        String activationURI = "http://localhost:4200/account/activate/"+userId+"/token/"+activationToken;
        Notify n = new Notify();
        EmailTemplate template = new EmailTemplate(name, activationURI);
        n.setConference_id(Constants.SERVER_CONFERENCE);
        n.setCreated_on(new Date());
        n.setEmail_message(template.getTemplate());
        n.setIs_broadcast("N");
        n.setMethod(Arrays.asList(Constants.SCHEME_EMAIL));
        n.setPriority(Constants.NotifyMethod.ACTIVATION.toString());
        n.setReceiver(Arrays.asList(email));
        n.setSender_id(Constants.SERVER_ID);
        n.setSubject(Constants.ACTIVATION_EMAIL_TITLE);
        n.setSender_name(Constants.SERVER_NAME);

        return n;
    }

    public String generateActivationToken(String uuid, String uid){
        String token = uuid+"~"+uid;
        final byte[] tokenBytes = token.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(tokenBytes);
    }

    public boolean verifyActivationToken(String token, String uid){
        String decodedToken = new String(Base64.getDecoder().decode(token));
        String [] splitTokens = decodedToken.split("~");
        if(splitTokens.length == 2 && splitTokens[1].equals(uid)) return true;

        return false;
    }

    @Async
    public void notifyAsync(Notify notifyPayLoad){
        notificationServiceProxy.notify(notifyPayLoad);
    }

    public int activateUser(String id){
        int i = authServiceRepo.update(Query.ACTIVATE_USER, id);
        authServiceRepo.update(Query.PURGE_TOKEN, id);
        return i;
    }



}
