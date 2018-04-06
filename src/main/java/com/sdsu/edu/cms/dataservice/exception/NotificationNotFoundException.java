package com.sdsu.edu.cms.dataservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotificationNotFoundException extends RuntimeException {


        public NotificationNotFoundException(String s){
            super(s);
        }

        public NotificationNotFoundException(int errCode){
            super(Integer.toString(errCode));
        }

}

