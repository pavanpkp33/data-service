package com.sdsu.edu.cms.dataservice.repository;


import com.google.gson.Gson;
import com.sdsu.edu.cms.common.models.cms.Conference;
import com.sdsu.edu.cms.common.models.user.User;
import com.sdsu.edu.cms.dataservice.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.*;

@Repository
public class UserServiceRepo implements DataAccessRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Object> findAll(String query, Object... params) {

        List<Conference> conferences = jdbcTemplate.query(query, params,  (rs, rowNum) ->  new Conference(rs.getString("cid"),
                rs.getString("cname"),
                rs.getString("caccronym"),
                0,
                null,
                rs.getTimestamp("start_date"),
                null,
                null,
               null,
                null,
                null,
                null,
                null,
                rs.getString("city"),
                null,
                rs.getString("submissions_enabled")));
                if(conferences.size() == 0)return Arrays.asList("-1");
         return  Arrays.asList(new Gson().toJson(conferences).toString());
    }

    @Override
    public List<List<String>> findOne(String query, Object... params) {
       return jdbcTemplate.query(query, params, (rs, rowNum)->{
            List<String> r = new ArrayList<>();
            r.add(rs.getString("cid"));
            r.add(rs.getString("role_id"));
            return r;


        });
    }

    @Override
    public Object findById(String query, String id) {
        List<User> user = jdbcTemplate.query(query, new String[]{id}, (rs, rowNum)->{
            User u = new User();
            u.setId(rs.getString("id"));
            u.setEmail(rs.getString("email"));
            u.setAddress1(rs.getString("address1"));
            u.setAddress2(rs.getString("address2"));
            u.setfirst_name(rs.getString("first_name"));
            u.setmiddle_name(rs.getString("middle_name"));
            u.setlast_name(rs.getString("last_name"));
            u.setTitle(rs.getString("title"));
            u.setCity(rs.getString("city"));
            u.setState(rs.getString("state"));
            u.setCountry(rs.getString("country"));
            u.setZipcode(rs.getInt("zipcode"));
            u.setAffiliation(rs.getString("affiliation"));
            u.setDepartment(rs.getString("department"));
            u.setDob(rs.getDate("dob"));
            u.setis_participating(rs.getString("is_participating"));
            u.setis_active(rs.getString("is_active"));
            u.setvalid(rs.getString("valid"));

            return u;
        });
        if(user.isEmpty() || user == null){
            throw new UserNotFoundException("User with entered ID not found");
        }
        return user.get(0);

    }

    @Override
    public int save(String query, Object... params) {
        return 0;
    }

    @Override
    public int update(String query, Object... params) {
        try{
            int i = jdbcTemplate.update(query, params);
            return i;
        }catch (Exception e){
            return -1;
        }
    }

}
