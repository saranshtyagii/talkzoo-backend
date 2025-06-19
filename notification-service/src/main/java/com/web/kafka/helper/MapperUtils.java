package com.web.kafka.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class MapperUtils {

    private static final ObjectMapper objectMapper =  new ObjectMapper();

    public static String convertObjectToString(Object object) {
        if (object == null) return null;
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception exception) {
            // push exception to Kafka or log it
            exception.printStackTrace();
        }
        return null;
    }

    // Generic method for any type
    public static <T> T convertStringToObject(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) return null;
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception exception) {
            // push exception to Kafka or log it
            exception.printStackTrace();
        }
        return null;
    }

    // Generic method for complex types like List<Map<String, Object>> etc.
    public static <T> T convertStringToObject(String json, TypeReference<T> typeReference) {
        if (json == null || json.isEmpty()) return null;
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception exception) {
            // push exception to Kafka or log it
            exception.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> convertStringToList(String json, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public static List<LogsEvents> convertStringToLogsEventsList(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, LogsEvents.class);
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            e.printStackTrace(); // or use proper logging
            return new ArrayList<>();
        }
    }



}
