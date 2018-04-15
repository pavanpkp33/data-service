package com.sdsu.edu.cms.dataservice.repository;


import com.google.gson.Gson;
import com.sdsu.edu.cms.common.models.review.Assignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class AssignmentServiceRepo implements DataAccessRepository{
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public List<Object> findAll(String query, Object... params) {
        List<Assignment> assignmentList = jdbcTemplate.query(query, params, (rs, rowNum) -> new Assignment(rs.getString(1),
        rs.getString(2),
        rs.getString(3),
        rs.getString(4),
        rs.getString(5),
        rs.getString(6)));

        return Arrays.asList(new Gson().toJson(assignmentList).toString());
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
        try{
            return  jdbcTemplate.update(query, params);

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public int update(String query, Object... params) {
        return 0;
    }
}
