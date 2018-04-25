package com.sdsu.edu.cms.dataservice.repository;

import com.google.gson.Gson;
import com.sdsu.edu.cms.common.models.review.Review;
import com.sdsu.edu.cms.common.models.review.Reviewers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Repository
public class ReviewServiceRepo implements DataAccessRepository{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Object> findAll(String query, Object... params) {
        List<Reviewers> reviewers = jdbcTemplate.query(query, params,  (rs, rowNum) -> new Reviewers(rs.getString("cid"),
                rs.getString("uid"),
                rs.getString("role_id"),
                rs.getString("first_name"),
                rs.getString("email"),
                "Y"));
        return Arrays.asList(new Gson().toJson(reviewers).toString());
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

        try {
            return  jdbcTemplate.update(query, params);

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public int update(String query, Object... params) {
        return 0;
    }

    public List<Review> findReview(String query, Object ...params){
        List<Review> reviews = jdbcTemplate.query(query, params, (rs, rowNum) -> new Review(rs.getString("rid"),
                rs.getString("sid"),
                null,
                rs.getString("uid"),
                rs.getString("first_name"),
                rs.getString("email"),
                rs.getString("review"),
                rs.getInt("score"),
                rs.getString("message_to_chair"),
                rs.getInt("confidence_score"),
                rs.getTimestamp("last_updated", Calendar.getInstance()),
                rs.getString("publish"),
                "Y"));
        return  reviews;
    }
}
