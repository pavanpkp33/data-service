package com.sdsu.edu.cms.dataservice.exception;



import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.Arrays;

@ControllerAdvice
@RestController
public class DataServiceExceptionHandler extends ResponseEntityExceptionHandler{


    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(Exception ex, WebRequest request) {
        ServiceResponse apiError;
        if(ex.getMessage().toString().equals("1062")){
            apiError = new ServiceResponse(Arrays.asList(request.getDescription(false)),"Duplicate User.");
            return new ResponseEntity(apiError, HttpStatus.CONFLICT);
        }
        apiError = new ServiceResponse(Arrays.asList(request.getDescription(false)),ex.getMessage());
        return new ResponseEntity(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLException.class)
    public final ResponseEntity<Object> handleSQLException(SQLException ex, WebRequest request) {
        if(ex.getErrorCode() == 1062){
            ServiceResponse apiError = new ServiceResponse(Arrays.asList(request.getDescription(false)),ex.getMessage());
            return new ResponseEntity(apiError, HttpStatus.CONFLICT);
        }
        ServiceResponse apiError = new ServiceResponse(Arrays.asList(request.getDescription(false)),ex.getMessage());
        return new ResponseEntity(apiError, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ServiceResponse apiError = new ServiceResponse(Arrays.asList(request.getDescription(false)),ex.getMessage());
        return new ResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
