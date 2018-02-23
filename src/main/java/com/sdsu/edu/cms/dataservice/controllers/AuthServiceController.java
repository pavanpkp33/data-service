package com.sdsu.edu.cms.dataservice.controllers;


import com.sdsu.edu.cms.dataservice.beans.DataServiceResponse;
import com.sdsu.edu.cms.dataservice.beans.User;
import com.sdsu.edu.cms.dataservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/v1/api", produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE})
public class AuthServiceController {
    @Autowired
    AuthService authService;
    @PostMapping("/auth/query")
    public DataServiceResponse queryUserName(@RequestBody Map<String, Object> payLoad, HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {

        return new DataServiceResponse(Arrays.asList(authService.findById(payLoad.get("email"))), "User Information");
    }

    @PostMapping("/auth/save")
    public DataServiceResponse saveUser(@RequestBody User user){
        System.out.println("cominh here");
        int i = authService.saveUser(user);
        return new DataServiceResponse(Arrays.asList(i), "Success");
    }

}
