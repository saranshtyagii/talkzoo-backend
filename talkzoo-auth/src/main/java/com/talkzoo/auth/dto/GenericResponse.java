package com.talkzoo.auth.dto;

import java.util.Date;

public class GenericResponse {

    private Object data;
    private String message;
    private Date timestamp;

    public void setResponse(Object data) {
        this.data = data;
    }

    public void setError(String message) {
        this.message = message;
        this.timestamp = new Date();
    }

}
