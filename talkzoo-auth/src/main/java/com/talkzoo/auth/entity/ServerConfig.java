package com.talkzoo.auth.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ServerConfig {

    @Id
    private String id;
    private String jwtSecretKey;
    private String jwtIssuer;
    private String apiKey;

}
