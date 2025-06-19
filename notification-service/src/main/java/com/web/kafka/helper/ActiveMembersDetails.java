package com.web.kafka.helper;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActiveMembersDetails {
    private String displayName;
    private char gender;
    private List<String> interests;
    private boolean active;
    private boolean engaged;
}
