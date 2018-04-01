package com.sdsu.edu.cms.dataservice.services;



import com.sdsu.edu.cms.common.models.user.User;
import com.sdsu.edu.cms.dataservice.exception.UserNotFoundException;
import com.sdsu.edu.cms.dataservice.repository.AuthServiceRepo;
import com.sdsu.edu.cms.dataservice.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

@Service
public class AuthService{
    @Autowired
    AuthServiceRepo authServiceRepo;

    public Object authenticateUser(Object id){

        return authServiceRepo.findOne(Query.GET_USER_BY_EMAIL, id);

    }

    public int saveUser(User user){

        return  authServiceRepo.save(Query.SAVE_USER, user.getArray());


    }





}
