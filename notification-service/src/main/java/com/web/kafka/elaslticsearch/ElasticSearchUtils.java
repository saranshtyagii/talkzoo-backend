package com.web.kafka.elaslticsearch;

import com.web.kafka.helper.LogsEvents;
import com.web.kafka.helper.MapperUtils;
import com.web.kafka.utils.RedisConstants;
import com.web.kafka.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

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
            List<LogsEvents> eventsLogs = new ArrayList<>();
            String previousLogs = redisUtils.get(RedisConstants.EVENTS_LOGS);

            if (StringUtils.isNotBlank(previousLogs)) {
                eventsLogs = MapperUtils.convertStringToList(previousLogs, LogsEvents.class);
            }
            eventsLogs.add(logsEvents);
            String logsMessageString = MapperUtils.convertObjectToString(eventsLogs);
            redisUtils.set(RedisConstants.EVENTS_LOGS, logsMessageString);
        } catch (Exception e) {
            log.error("PUSH_EXCEPTION", e);
        }
    }

    public void pushException(LogsEvents logsEvents) {
        try {
            logsEvents.setTimestamp(new Date());
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
            Map<String, List<String>> logsMap = new HashMap<>();
            List<String> previousLogsMessage = new ArrayList<>();
            // read previous logs
            String previousLogs = redisUtils.get(RedisConstants.ESLOGSMAP);
            if(StringUtils.isNotBlank(previousLogs)) {
                logsMap = MapperUtils.convertStringToObject(previousLogs, Map.class);
                previousLogsMessage.addAll(logsMap.get(type));
            }
            // save latest logs
            previousLogsMessage.add(message);
            logsMap.put(type, previousLogsMessage);
            String jsonString = MapperUtils.convertObjectToString(logsMap);
            redisUtils.set(RedisConstants.ESLOGSMAP, jsonString, RedisUtils.ONE_WEEK);

        } catch (Exception e) {
            log.error("PUSH_EXCEPTION_MESSAGE", e);
        }
    }

    public void pushException(Map<String, String> logsMap) {
        try {
            String key = RedisConstants.ESLOGSMAP;
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
