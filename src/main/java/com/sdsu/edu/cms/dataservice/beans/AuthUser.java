package com.sdsu.edu.cms.dataservice.beans;

public class AuthUser {

    private String id;
    private String email;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthUser(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
