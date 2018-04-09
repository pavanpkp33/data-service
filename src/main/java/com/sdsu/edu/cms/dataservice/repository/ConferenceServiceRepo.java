package com.sdsu.edu.cms.dataservice.repository;

import com.google.gson.Gson;
import com.sdsu.edu.cms.common.models.cms.Conference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class ConferenceServiceRepo implements DataAccessRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

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
        List<Conference> conferences = jdbcTemplate.query(query, new Object[]{id},  (rs, rowNum) ->  new Conference(rs.getString("cid"),
                rs.getString("cname"),
                rs.getString("caccronym"),
                rs.getInt("cyear"),
                rs.getString("chair_uid"),
                rs.getTimestamp("start_date"),
                rs.getTimestamp("end_date"),
                rs.getString("web_link"),
                rs.getString("valid"),
                rs.getString("contact"),
                rs.getString("about"),
                rs.getString("banner_url"),
                rs.getString("venue"),
                rs.getString("city"),
                rs.getString("country"),
                rs.getString("submissions_enabled")));
        if(conferences.size() == 0) return "-1";
        return new Gson().toJson(conferences.get(0)).toString();
    }

    @Override
    public int save(String query, Object... params) {
        try{
            int i = jdbcTemplate.update(query, params);
            return i;
        }catch (DuplicateKeyException e){
            return -2;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public int saveAndGetId(String query, String trackName, String id){
        KeyHolder key = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement ps = connection.prepareStatement(query,
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, trackName);
                ps.setString(2, id);
                return ps;
            }
        }, key);

        return (int)key.getKey().longValue();



    }

    public int[] batchInsert(String query, List<Object[]> args){
        int res[] = jdbcTemplate.batchUpdate(query, args);
        return res;
    }
    @Override
    public int update(String query, Object... params) {
        return 0;
    }
}
