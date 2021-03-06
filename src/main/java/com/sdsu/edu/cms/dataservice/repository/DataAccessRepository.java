package com.sdsu.edu.cms.dataservice.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataAccessRepository   {

    List<Object> findAll(String query, Object ...params);

    Object findOne(String query, Object ...params);

    Object findById(String query, String id);

    int save(String query, Object ...params);

    int update(String query, Object ...params);
}
