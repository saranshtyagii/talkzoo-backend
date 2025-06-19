package com.talkzoo.auth.payloads;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserMetaData {
    private String displayName;
    private char gender;
    private List<String> interestToTalk;
}
