package com.sdsu.edu.cms.dataservice.exception;


import com.sdsu.edu.cms.dataservice.beans.DataServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
@RestController
public class DataServiceExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        DataServiceResponse apiError = new DataServiceResponse(Arrays.asList(request.getDescription(false)),ex.getMessage());
        return new ResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(Exception ex, WebRequest request) {
        DataServiceResponse apiError = new DataServiceResponse(Arrays.asList(request.getDescription(false)),ex.getMessage());
        return new ResponseEntity(apiError, HttpStatus.NOT_FOUND);
    }


}
