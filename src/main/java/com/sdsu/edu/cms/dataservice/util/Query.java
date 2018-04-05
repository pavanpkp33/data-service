package com.sdsu.edu.cms.dataservice.util;

public class Query {

    /**
     *  Auth Service Queries
     */
    public static final String GET_USER_BY_EMAIL = "SELECT * FROM USERS WHERE EMAIL = ?";
    public static final String SAVE_USER = "INSERT INTO USERS (id, first_name, last_name, middle_name, title, email, " +
            "password, address1, address2, city, state, country, zipcode, affiliation, department, dob, is_participating) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String GET_USER_BY_ID = "SELECT * FROM USERS WHERE ID = ?";
    public static final String SAVE_ACTIVATION_TOKEN = "INSERT INTO USER_ACTIVATION (id, user_id, token) VALUES ( ?,?,?)";
    public static final String ACTIVATE_USER = "UPDATE USERS SET is_active = 'Y' WHERE id = ?";
    public static final String PURGE_TOKEN = "DELETE FROM USER_ACTIVATION WHERE user_id = ?";


}
