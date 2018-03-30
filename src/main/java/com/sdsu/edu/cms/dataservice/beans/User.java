package com.sdsu.edu.cms.dataservice.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class User {
    @JsonProperty("id")
    private String id;
    @JsonProperty("first_name")
    private String  first_name;
    @JsonProperty("last_name")
    private String last_name;
    @JsonProperty("middle_name")
    private String middle_name = null;
    @JsonProperty("title")
    private String title;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("address1")
    private String address1;
    @JsonProperty("address2")
    private String address2 = null;
    @JsonProperty("city")
    private String city;
    @JsonProperty("state")
    private String state;
    @JsonProperty("country")
    private String country;
    @JsonProperty("zipcode")
    private int zipcode;
    @JsonProperty("affiliation")
    private String affiliation;
    @JsonProperty("department")
    private String department;
    @JsonProperty("dob")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    @JsonProperty("is_participating")
    private String is_participating;
    @JsonProperty("valid")
    private String valid = "Y";
    @JsonProperty("is_active")
    private String is_active = "N";

    public String getis_active() {
        return is_active;
    }

    public void setis_active(String is_active) {
        this.is_active = is_active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getfirst_name() {
        return first_name;
    }

    public void setfirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getlast_name() {
        return last_name;
    }

    public void setlast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getmiddle_name() {
        return middle_name;
    }

    public void setmiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getis_participating() {
        return is_participating;
    }

    public void setis_participating(String is_participating) {
        this.is_participating = is_participating;
    }

    public String getvalid() {
        return valid;
    }

    public void setvalid(String valid) {
        this.valid = valid;
    }

    public Object[] getArray(){
        Object[] arr = {
                this.id,
                this.first_name,
                this.last_name,
                this.middle_name,
                this.title,
                this.email,
                this.password,
                this.address1,
                this.address2,
                this.city,
                this.state,
                this.country,
                this.zipcode,
                this.affiliation,
                this.department,
                this.dob,
                this.is_participating,
                this.valid,
                this.is_active
        };

        return arr;
    }
}
