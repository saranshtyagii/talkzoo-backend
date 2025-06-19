package com.web.kafka.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.web.kafka.helper.LogsEvents;
import com.web.kafka.helper.MapperUtils;
import com.web.kafka.utils.RedisConstants;
import com.web.kafka.utils.RedisUtils;
import org.apache.catalina.mapper.Mapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/logs")
public class LogsController {


    private final RedisUtils redisUtils;

    public LogsController(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @GetMapping("/find-all")
    public ResponseEntity<Map<String, List<String>>> getLogs(@RequestParam String apiKey) {
        Map<String, List<String>> logsMap = new HashMap<>();
        try {
         if(apiKey == null || apiKey.isEmpty()) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }
         if(apiKey.compareTo("pc@7011") != 0) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }
         String data = redisUtils.get(RedisConstants.ESLOGSMAP);
         if(StringUtils.isNotBlank(data)) {
             logsMap = MapperUtils.convertStringToObject(data, Map.class);
         }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(logsMap);
    }

    @GetMapping("find-all-events")
    public ResponseEntity<String> getLogsEvents(@RequestParam String apiKey, @RequestParam String eventId) {
        try {
            // API key validation
            if (StringUtils.isBlank(apiKey) || !apiKey.equals("pc@7011")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Fetch and parse logs
            String logsInRedis = redisUtils.get(RedisConstants.EVENTS_LOGS);
            List<LogsEvents> eventsLogs = new ArrayList<>();

            if(StringUtils.isNotBlank(logsInRedis)) {
                eventsLogs = MapperUtils.convertStringToLogsEventsList(logsInRedis);
            }

            return ResponseEntity.ok(MapperUtils.convertObjectToString(eventsLogs));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).body("[]");
        }
    }


}
