package com.sdsu.edu.cms.dataservice.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationServiceRepo implements DataAccessRepository{

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
        return 0;
    }

    @Override
    public int update(String query, Object... params) {
        return 0;
    }
}
