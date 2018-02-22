package com.sdsu.edu.cms.dataservice.beans;


import java.util.List;

public class DataServiceResponse {

    private List<Object> data;
    private String message;

    public DataServiceResponse(List<Object> data, String message) {
        this.data = data;
        this.message = message;
    }



    public List<Object> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
