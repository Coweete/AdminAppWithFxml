package com.fxmlspringtest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by jonatan on 2016-04-28.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class XSRFToken implements Serializable{

    private String token;

    public XSRFToken(){

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
