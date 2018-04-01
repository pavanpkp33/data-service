package com.sdsu.edu.cms.dataservice.controllers;


import com.sdsu.edu.cms.common.models.response.DataServiceResponse;
import com.sdsu.edu.cms.common.models.user.User;
import com.sdsu.edu.cms.dataservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1", produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE})
public class AuthServiceController {
    @Autowired
    AuthService authService;
    @PostMapping("/auth/query")
    public DataServiceResponse queryUserName(@RequestBody String email){

        return new DataServiceResponse(Arrays.asList(authService.authenticateUser(email)), "User Information");
    }

    @PostMapping("/auth/save")
    public DataServiceResponse saveUser(@RequestBody User user){
        authService.saveUser(user);
        return new DataServiceResponse(Arrays.asList(true), "User registered successfully");
    }


}
