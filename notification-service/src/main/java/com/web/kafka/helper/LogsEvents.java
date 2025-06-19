package com.web.kafka.helper;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class LogsEvents {

    private String message;
    private String eventId;
    private String type;
    private String environment;
    private String username;
    private String userId;
    private String userDetailsString;
    private String logType;
    private Date timestamp;

}
