package com.sdsu.edu.cms.dataservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String s){
        super(s);
    }

    public UserNotFoundException(int errCode){
        super(Integer.toString(errCode));
    }
}
