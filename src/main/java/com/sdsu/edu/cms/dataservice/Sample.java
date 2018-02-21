package com.sdsu.edu.cms.dataservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Sample {

    @GetMapping("/hi")
    public String helloWorld(){

        return "Hi";
    }
}
