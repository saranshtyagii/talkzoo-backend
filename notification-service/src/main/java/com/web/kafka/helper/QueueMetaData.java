package com.web.kafka.helper;

import lombok.Data;

import java.util.Date;

@Data
public class QueueMetaData {

    LogType type;
    String logsMessage;
    Date timestamp;
    String environment;

}
