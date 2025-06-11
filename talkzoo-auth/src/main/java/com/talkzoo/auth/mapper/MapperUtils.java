package com.talkzoo.auth.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talkzoo.auth.entity.UserDocument;
import com.talkzoo.auth.payloads.RegisterUser;

public class MapperUtils {

    private static final ObjectMapper objectMapper =  new ObjectMapper();

    public static UserDocument converRegisterUsertoUserDocument(RegisterUser registerUser) {
        return new UserDocument();
    }

    public static String convertObjectToString(Object object) {
        if (object == null) return null;
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception exception) {
            // push exception to kafka
        }
        return null;
    }

}
