package com.sdsu.edu.cms.dataservice.services;


import com.sdsu.edu.cms.dataservice.beans.User;
import com.sdsu.edu.cms.dataservice.repository.AuthServiceRepo;
import com.sdsu.edu.cms.dataservice.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService{
    @Autowired
    AuthServiceRepo authServiceRepo;

    public Object findById(Object id){

        return authServiceRepo.findOne(Query.GET_USER_BY_EMAIL, id);

    }

    public int saveUser(User user){

        return  authServiceRepo.save(Query.SAVE_USER, user.getArray());


    }

}
