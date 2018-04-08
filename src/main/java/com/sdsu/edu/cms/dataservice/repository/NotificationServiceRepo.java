package com.sdsu.edu.cms.dataservice.repository;

import com.google.gson.Gson;
import com.sdsu.edu.cms.common.models.notification.NotifyDBModel;
import com.sdsu.edu.cms.common.models.user.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Repository
public class NotificationServiceRepo implements DataAccessRepository{


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Object> findAll(String query, Object... params) {
        List<NotifyDBModel> result = jdbcTemplate.query(query, params,  (rs, rowNum) ->  new NotifyDBModel(rs.getString("notification_id"),
                rs.getString("cid"),
                rs.getString("sender_uid"),
                rs.getString("sender_name"),
                rs.getString("title"),
                rs.getString("receiver_email"),
                rs.getString("notification_type"),
                rs.getString("body"),
                rs.getTimestamp("sent_on", Calendar.getInstance()),
                rs.getString("priority"),
                rs.getString("valid"),
                rs.getString("has_seen"),
                rs.getString("is_broadcast")));

        return Arrays.asList(new Gson().toJson(result).toString());
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
