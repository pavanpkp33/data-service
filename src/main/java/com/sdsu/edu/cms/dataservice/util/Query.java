package com.sdsu.edu.cms.dataservice.util;

public class Query {

    /**
     *  Auth Service Queries
     */
    public static final String GET_USER_BY_EMAIL = "SELECT * FROM USERS WHERE EMAIL = ?";
    public static final String SAVE_USER = "INSERT INTO USERS (id, first_name, last_name, middle_name, title, email, " +
            "password, address1, address2, city, state, country, zipcode, affiliation, department, dob, is_participating, is_active) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String GET_USER_BY_ID = "SELECT * FROM USERS WHERE ID = ?";
    public static final String SAVE_ACTIVATION_TOKEN = "INSERT INTO USER_ACTIVATION (id, user_id, token) VALUES ( ?,?,?)";
    public static final String ACTIVATE_USER = "UPDATE USERS SET is_active = 'Y' WHERE id = ?";
    public static final String PURGE_TOKEN = "DELETE FROM USER_ACTIVATION WHERE user_id = ?";
    public static final String SAVE_NOTIFICATION = "INSERT INTO NOTIFICATIONS (notification_id, title, body, sent_on, sender_uid, receiver_email, is_broadcast, cid, priority," +
            " notification_type, sender_name) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    public static final String GET_NOTIFICATION = "SELECT * FROM NOTIFICATIONS ";

    public static final String CREATE_CONFERENCE = "INSERT INTO CONFERENCE (cid, cname, caccronym, cyear, chair_uid, start_date, end_date, web_link, contact, about, banner_url, venue," +
            "city, country) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String ADD_ROLE = "INSERT INTO CONF_ROLES (cid, uid, role_id ) VALUES (?,?,?)";
    //Get the roles of user for all conferences
    public static final String GET_ROLE_CONF = "select c.cid, c.cname, r.role_id from conf_roles r, conference c, users u where c.cid = r.cid AND r.uid = u.id AND u.id = ?";
    public static final String ADD_TRACK = "INSERT INTO tracks (tname, cid) VALUES (?,?)";
    public static final String ADD_KEYWORDS_TRACK = "INSERT INTO tracks_keywords (track_id, keyword) VALUES (?,?)";

    public static final String GET_CONF_BY_NAME = "SELECT * FROM conference where caccronym = ? AND valid = 'Y'";
    public static final String GET_TRACKS = "SELECT * FROM tracks WHERE cid = ? AND VALID = 'Y'";
    public static final String GET_TRACKS_KW = "SELECT keyword FROM tracks_keywords WHERE track_id = ? AND VALID = 'Y'";


    public static final String ADD_SUBMISSION = "INSERT INTO submissions (sid, cid, title, submission_date, submit_author_id, track_id, abstract_text, last_updated, decision_status," +
            " group_app) VALUES(?,?,?,?,?,?,?,?,?,?)";
    public static final String ADD_CONF_SUB_USERS = "INSERT INTO conf_sub_users (cid, sid, uid, is_corresponding_user) VALUES (?,?,?,?)";
    public static final String ADD_KEYWORDS_SUBMISSION = "INSERT INTO keywords (sid, keyword) VALUES (?,?)";

    public static final String GET_TRACKS_KW_FOR_CID = "SELECT a.tid, b.keyword FROM tracks a LEFT OUTER JOIN tracks_keywords b ON a.tid = b.track_id  WHERE a.cid = ? AND a.valid = 'Y' AND b.valid = 'Y'";



}
