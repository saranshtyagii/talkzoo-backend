package com.talkzoo.auth.helpers;

import com.talkzoo.auth.payloads.UserMetaData;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ActiveUserMetaData {

    private String userId;
    private UserMetaData userMetaData;
    private boolean activeStatus;

}
