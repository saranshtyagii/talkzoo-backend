package com.talkzoo.auth.entity;

import com.talkzoo.auth.entity.helper.DeviceDetails;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class UserDocument {

    private String id;

    private String fullName;

    private String username;
    private String email;
    private String password;

    private List<String> listOfFriends;

    @CreatedDate
    private Date creationDate;
    @LastModifiedDate
    private Date updateDate;

    private boolean showDetails;
    private boolean userVerified;
    private boolean userDeleted;

    private Map<String, DeviceDetails>  deviceDetails;

}
