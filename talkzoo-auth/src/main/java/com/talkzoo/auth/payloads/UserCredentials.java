package com.talkzoo.auth.payloads;

import com.talkzoo.auth.entity.helper.DeviceDetails;
import lombok.Data;

@Data
public class UserCredentials {

    private String username;
    private String password;

//    private DeviceDetails deviceDetails;

}
