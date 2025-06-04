package com.talkzoo.auth.dto;

import lombok.Data;

@Data
public class UserDetailResponse {

    private String profileImageUrl;
    private String fullName;
    private String email;
    private String username;

}
