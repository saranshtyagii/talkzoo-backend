package com.web.kafka.elaslticsearch;

import com.web.kafka.helper.LogsEvents;
import com.web.kafka.helper.MapperUtils;
import com.web.kafka.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ElasticSearchUtils {

    private final RedisUtils redisUtils;

    public ElasticSearchUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    public void push(Map<String, String> logsMap) {
        try {
            String key = "EVENTS_LOGS_MAP";
            // fetch previous logs
            String previousLogs = redisUtils.get(key).toString();
            if(StringUtils.isNotBlank(previousLogs)) {
                Map<String, String> previousLogsMap = MapperUtils.convertStringToObject(previousLogs,  Map.class);
                logsMap.putAll(previousLogsMap);
            }
            String logsMessageString = MapperUtils.convertObjectToString(logsMap);
            redisUtils.set(key, logsMessageString, RedisUtils.ONE_WEEK);
        } catch (Exception e) {
            log.error("PUSH_EXCEPTION_MAP", e);
        }
    }

    public void push(LogsEvents logsEvents) {
        try {
            String key = "EVENTS_LOGS";
            String previousLogs = redisUtils.get(key).toString();
            if(StringUtils.isNotBlank(previousLogs)) {
                List<LogsEvents> previousLogsMap = MapperUtils.convertStringToObject(previousLogs, List.class);
                previousLogsMap.add(logsEvents);
            }
            String logsMessageString = MapperUtils.convertObjectToString(logsEvents);
            redisUtils.set(key, logsMessageString, RedisUtils.ONE_WEEK);
        } catch (Exception e) {
            log.error("PUSH_EXCEPTION", e);
        }
    }

    public void pushException(LogsEvents logsEvents) {
        try {
            String key = "TALK_ZOO_EXCEPTION";
            String previousLogs = redisUtils.get(key).toString();
            if(StringUtils.isNotBlank(previousLogs)) {
                List<LogsEvents> previousLogsMap = MapperUtils.convertStringToObject(previousLogs, List.class);
                previousLogsMap.add(logsEvents);
            }
            String exceptionMessageString = MapperUtils.convertObjectToString(logsEvents);
            redisUtils.set(key, exceptionMessageString, RedisUtils.ONE_WEEK);
        } catch (Exception e) {
            log.error("PUSH_EXCEPTION", e);
        }
    }

    public void pushException(String type, String message) {
        try {
            Date now = new Date();
            message.concat(now.toString());
            String value = type +"_" + message;
            redisUtils.set(type, value, RedisUtils.ONE_WEEK);
        } catch (Exception e) {
            log.error("PUSH_EXCEPTION_MESSAGE", e);
        }
    }

    public void pushException(Map<String, String> logsMap) {
        try {
            String key = "TALK_ZOO_EXCEPTION_MAP";
            String previousLogs = redisUtils.get(key).toString();
            if(StringUtils.isNotBlank(previousLogs)) {
                Map<String, String> previousLogsMap = MapperUtils.convertStringToObject(previousLogs, Map.class);
                previousLogsMap.putAll(logsMap);
            }
            String exceptionMessageString = MapperUtils.convertObjectToString(logsMap);
            redisUtils.set(key, exceptionMessageString, RedisUtils.ONE_WEEK);
        } catch (Exception e) {
            log.error("PUSH_EXCEPTION_MAP", e);
        }
    }

}
