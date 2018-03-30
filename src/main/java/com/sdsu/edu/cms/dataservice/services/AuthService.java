package com.sdsu.edu.cms.dataservice.services;


import com.sdsu.edu.cms.dataservice.beans.User;
import com.sdsu.edu.cms.dataservice.repository.AuthServiceRepo;
import com.sdsu.edu.cms.dataservice.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

    public int updateUser(User user, String id){
        String query = buildQuery(user, id);
        System.out.println(query);
        int i = authServiceRepo.update(query, null);
        System.out.println(i);
        return 0;
    }

    public int deleteUser(String id){
        return 0;
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
