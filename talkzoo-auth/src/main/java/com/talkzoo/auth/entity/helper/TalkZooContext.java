package com.talkzoo.auth.entity.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TalkZooContext {

    private final String userHavingToken;
    private final String token;
    private final String userId;
    private final String userName;
    private final String iOS;
    private final String android;
    private final String web;
    private final String isUserLogin;

}
