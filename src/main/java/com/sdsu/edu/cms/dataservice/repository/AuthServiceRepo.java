package com.sdsu.edu.cms.dataservice.repository;


import com.google.gson.Gson;
import com.sdsu.edu.cms.common.models.user.AuthUser;
import com.sdsu.edu.cms.dataservice.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthServiceRepo implements DataAccessRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Object> findAll(String query, Object... params) {
        return null;
    }

    @Override
    public Object findOne(String query, Object... params) {
       List<AuthUser> user = jdbcTemplate.query(query, params,  (rs, rowNum) ->  new AuthUser(rs.getString("id"),
                rs.getString("email"),
                rs.getString("password"),
               rs.getString("valid"),
               rs.getString("is_active")));
        if(user.isEmpty() || user == null){
            throw new UserNotFoundException("User with entered ID not found");
        }
        return new Gson().toJson(user.get(0)).toString();
    }

    @Override
    public Object findById(String query, String id) {
        return null;
    }

    @Override
    public int save(String query, Object... params){
        try{
            int i = jdbcTemplate.update(query, params);
            return i;
        }catch (DuplicateKeyException e){
            throw new UserNotFoundException(1062);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return -1;
        }

    }

    @Override
    public int update(String query, Object... params) {
        try{
            int i = jdbcTemplate.update(query, params);
            return i;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return -1;
        }

    }
}
