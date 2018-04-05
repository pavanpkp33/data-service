package com.sdsu.edu.cms.dataservice.controllers;


import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.common.models.user.User;
import com.sdsu.edu.cms.dataservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1")
public class UserServiceController {

    @Autowired
    UserService userService;

    @PostMapping("/users/update")
    public ServiceResponse updateUser(@RequestBody User user, @RequestParam Map<String, String> id){
        userService.updateUser(user, id.get("id"));
        return new ServiceResponse(Arrays.asList(true), "User updated successfully");
    }

    @PostMapping("/users/delete")
    public ServiceResponse deleteUser(@RequestParam Map<String, String> payLoad){

        userService.deleteUser(payLoad.get("id"));
        return null;
    }

    @PostMapping("/users")
    public ServiceResponse findUserById(@RequestParam Map<String, String> id){
        User u = userService.findByUid(id.get("id"));
        return new ServiceResponse(Arrays.asList(u), "Query successful.");
    }

}
