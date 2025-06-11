package com.talkzoo.auth.payloads;

import com.talkzoo.auth.entity.helper.DeviceDetails;
import lombok.Data;

@Data
public class RegisterUser {

    private String fullName;
    private String username;
    private String email;
    private String password;

}
