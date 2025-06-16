package com.talkzoo.auth.payloads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceDetails {

    private String deviceId;
    private String deviceName;
    private String ipAddress;

}
