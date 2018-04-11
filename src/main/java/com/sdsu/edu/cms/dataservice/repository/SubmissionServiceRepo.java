package com.sdsu.edu.cms.dataservice.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class SubmissionServiceRepo implements DataAccessRepository{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Object> findAll(String query, Object... params) {
       return null;
    }

    @Override
    public Object findOne(String query, Object... params) {
        HashMap<Integer, HashSet<String>> set =  jdbcTemplate.query(query, params, rs -> {
            HashMap<Integer, HashSet<String>> data = new HashMap<>();
            HashSet<String> set1;
            while(rs.next()){
                if(data.containsKey(rs.getInt(1))){
                    set1 =  data.get(rs.getInt(1));
                    set1.add(rs.getString(2));
                    data.put(rs.getInt(1), set1);
                }else{
                    set1 = new HashSet<>();
                    set1.add(rs.getString(2));
                    data.put(rs.getInt(1), set1);
                }
            }

            return data;
        });
        return set;
    }

    @Override
    public Object findById(String query, String id) {
        return null;
    }

    @Override
    public int save(String query, Object... params) {
        try{
            int i = jdbcTemplate.update(query, params);
            return i;
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public int update(String query, Object... params) {
        return 0;
    }
}
