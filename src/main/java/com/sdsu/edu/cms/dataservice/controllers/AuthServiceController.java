package com.sdsu.edu.cms.dataservice.controllers;


import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.common.models.user.User;
import com.sdsu.edu.cms.dataservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1", produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE})
public class AuthServiceController {
    @Autowired
    AuthService authService;
    @PostMapping("/auth/query")
    public ServiceResponse queryUserName(@RequestBody String email){

        return new ServiceResponse(Arrays.asList(authService.authenticateUser(email)), "User Information");
    }

    @PostMapping("/auth/save")
    public ServiceResponse saveUser(@RequestBody User user){
        authService.saveUser(user);
        return new ServiceResponse(Arrays.asList(true), "User registered successfully");
    }

    @PostMapping("/auth/activate")
    public  ServiceResponse activateUser(@RequestParam Map<String, String> map){
        String id = map.get("id"), token = map.get("token");
        if(authService.verifyActivationToken(token, id)){
               int code = authService.activateUser(id);
               if( code != 1){
                   return new ServiceResponse(Arrays.asList(false), "-1");
               }else{
                   return new ServiceResponse(Arrays.asList(true), "1");
               }

        }else{
            return new ServiceResponse(Arrays.asList(false), "-2");
        }

    }


}
