package com.sdsu.edu.cms.dataservice.services;


import com.sdsu.edu.cms.common.models.user.User;
import com.sdsu.edu.cms.dataservice.exception.UserNotFoundException;
import com.sdsu.edu.cms.dataservice.repository.UserServiceRepo;
import com.sdsu.edu.cms.dataservice.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

@Service
public class UserService {
    @Autowired
    UserServiceRepo userServiceRepo;

    public int updateUser(User user, String id){
        String query = buildQuery(user, id);
        int i = userServiceRepo.update(query, null);
        if(i == 0) throw new UserNotFoundException("No valid user associated with given ID");
        return i;
    }

    public int deleteUser(String id){
        return 0;
    }

    public User findByUid(String id){
        User user = (User) userServiceRepo.findById(Query.GET_USER_BY_ID, id);

        return user;
    }

    public String buildQuery(User user, String id){
        String query = "UPDATE USERS SET ";
        boolean flag = true;
        for (Field field : user.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(user);
                if(value != null && !value.toString().equals("0")){
                    if(field.getName().equals("dob")){
                        query += ", "+ field.getName() +" = \""+new SimpleDateFormat("yyyy-MM-dd").format(value)+ "\" "; continue;
                    }
                    if(flag){
                        query += field.getName() +" = \""+value.toString()+"\"";
                        flag=false;
                    }else{
                        query += ", "+ field.getName() +" = \""+value.toString()+ "\" ";
                    }

                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        query += "WHERE id = \""+id+"\"";
        return query;
    }
}