package com.sdsu.edu.cms.dataservice.controllers;


import com.sdsu.edu.cms.common.models.response.DataServiceResponse;
import com.sdsu.edu.cms.common.models.user.User;
import com.sdsu.edu.cms.dataservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1", produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE})
public class UserServiceController {

    @Autowired
    UserService userService;

    @PostMapping("/users/update/{id}")
    public DataServiceResponse updateUser(@RequestBody User user, @PathVariable String id){
        userService.updateUser(user, id);
        return new DataServiceResponse(Arrays.asList(true), "User updated successfully");
    }

    @PostMapping("/users/delete/{id}")
    public DataServiceResponse deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return null;
    }

    @PostMapping("/users/{id}")
    public DataServiceResponse findUserById(@PathVariable String id){
        User u = userService.findByUid(id);
        return new DataServiceResponse(Arrays.asList(u), "Query successful.");
    }

}
