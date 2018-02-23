package com.sdsu.edu.cms.dataservice.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class User {
    @JsonProperty("id")
    private String id;
    @JsonProperty("first_name")
    private String  firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("middle_name")
    private String middleName = null;
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
    private String isParticipating;
    @JsonProperty("is_valid")
    private String isValid = "Y";
    @JsonProperty("is_active")
    private String isActive = "N";

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
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

    public String getIsParticipating() {
        return isParticipating;
    }

    public void setIsParticipating(String isParticipating) {
        this.isParticipating = isParticipating;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public Object[] getArray(){
        Object[] arr = {
                this.id,
                this.firstName,
                this.lastName,
                this.middleName,
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
                this.isParticipating,
                this.isValid,
                this.isActive
        };

        return arr;
    }
}
