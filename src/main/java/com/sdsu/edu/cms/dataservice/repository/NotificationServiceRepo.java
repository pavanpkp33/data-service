package com.sdsu.edu.cms.dataservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationServiceRepo implements DataAccessRepository{


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Object> findAll(String query, Object... params) {
        return null;
    }

    @Override
    public Object findOne(String query, Object... params) {
        return null;
    }

    @Override
    public Object findById(String query, String id) {
        return null;
    }

    @Override
    public int save(String query, Object... params) {
        int i;
        try {
            i = jdbcTemplate.update(query, params);

        }catch (Exception e){
            e.printStackTrace();
            i = -1;
            return i;
        }

        return i;
    }

    @Override
    public int update(String query, Object... params) {
        return jdbcTemplate.update(query, params);
    }
}
