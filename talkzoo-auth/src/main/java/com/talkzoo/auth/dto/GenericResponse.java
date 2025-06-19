package com.talkzoo.auth.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GenericResponse {

    private Object data;
    private String message;
    private Date timestamp;

    public void setResponse(Object data) {
        this.data = data;
        this.timestamp = new Date();
    }

    public void setError(Object data, String message) {
        this.data = data;
        this.message = message;
        this.timestamp = new Date();
    }

    public void setError(String message) {
        this.message = message;
        this.timestamp = new Date();
    }

    public void setResponse(Object data, String message) {
        this.data = data;
        this.message = message;
        this.timestamp = new Date();
    }

}
