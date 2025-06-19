package com.talkzoo.auth.controller;

import com.talkzoo.auth.dto.GenericResponse;
import com.talkzoo.auth.helpers.ActiveUserMetaData;
import com.talkzoo.auth.payloads.UserMetaData;
import com.talkzoo.auth.payloads.RegisterUser;
import com.talkzoo.auth.payloads.UserCredentials;
import com.talkzoo.auth.security.JwtTokenUtils;
import com.talkzoo.auth.security.JwtUserDetailsServices;
import com.talkzoo.auth.services.AbstractServices.AuthenticationServices;
import com.web.kafka.elaslticsearch.ElasticSearchUtils;
import com.web.kafka.helper.ActiveMembersDetails;
import com.web.kafka.helper.LogsEvents;
import com.web.kafka.helper.MapperUtils;
import com.web.kafka.queueservice.KafkaUtils;
import com.web.kafka.utils.RedisConstants;
import com.web.kafka.utils.RedisUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationServices authenticationServices;
    private final JwtTokenUtils jwtTokenUtils;
    private final JwtUserDetailsServices jwtUserDetailsServices;
    private final AuthenticationManager authenticationManager;
    private final ElasticSearchUtils elasticSearchUtils;
    private final RedisUtils redisUtils;
    private final KafkaUtils kafkaUtils;

    public AuthController(AuthenticationServices authenticationServices, JwtTokenUtils jwtTokenUtils, JwtUserDetailsServices jwtUserDetailsServices, AuthenticationManager authenticationManager, ElasticSearchUtils elasticSearchUtils, RedisUtils redisUtils, KafkaUtils kafkaUtils) {
        this.authenticationServices = authenticationServices;
        this.jwtTokenUtils = jwtTokenUtils;
        this.jwtUserDetailsServices = jwtUserDetailsServices;
        this.authenticationManager = authenticationManager;
        this.elasticSearchUtils = elasticSearchUtils;
        this.redisUtils = redisUtils;
        this.kafkaUtils = kafkaUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUser registerUser) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse.setResponse(authenticationServices.registerUser(registerUser));
        } catch (Exception e) {
            genericResponse.setError(registerUser, "Unable to register your account at that moment! Please try again later.");
            elasticSearchUtils.pushException("REGISTER_USER", e.getMessage());
        }
        return ResponseEntity.ok(MapperUtils.convertObjectToString(genericResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody UserCredentials userCredentials) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword())
                );
            } catch (Exception e) {
                genericResponse.setError(userCredentials, "Invalid username or password!");
            }

            UserDetails userDetails = jwtUserDetailsServices.loadUserByUsername(userCredentials.getUsername());
            String token = jwtTokenUtils.generateToken(userDetails);
            genericResponse.setResponse(token);
            elasticSearchUtils.push(LogsEvents.builder()
                            .username(userDetails.getUsername())
                            .message("LOGIN_SUCCESS")
                    .build());
        } catch (Exception e) {
            genericResponse.setError(userCredentials, "Unable to Login!");
            elasticSearchUtils.pushException("LOGIN_USER", e.getMessage());
        }
        return ResponseEntity.ok(MapperUtils.convertObjectToString(genericResponse));
    }


    @PostMapping("/login-guest")
    public ResponseEntity<String> loginGuest(@RequestBody UserMetaData metaData) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            String uuid = UUID.randomUUID().toString();
            ActiveUserMetaData activeUserMetaData = new ActiveUserMetaData(uuid, metaData, true);
            String activeUSerString = MapperUtils.convertObjectToString(activeUserMetaData);
            redisUtils.set(RedisConstants.ACTIVE_USER_INFO, activeUSerString, RedisUtils.ONE_HOUR);
            Map<String, Object> guestMap = new HashMap<>();
            guestMap.put(uuid, ActiveMembersDetails.builder()
                            .displayName(metaData.getDisplayName())
                            .gender(metaData.getGender())
                            .interests(metaData.getInterestToTalk())
                            .active(true)
                            .engaged(false)
                    .build());
            kafkaUtils.pushToQueue(guestMap);
            genericResponse.setResponse(uuid);
        } catch (Exception e) {
            elasticSearchUtils.pushException("LOGIN_GUEST", e.getMessage());
            genericResponse.setError(e.getMessage(), e.getMessage());
        }
        return ResponseEntity.ok(MapperUtils.convertObjectToString(genericResponse));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<GenericResponse> validateToken(@RequestParam String token) {
        GenericResponse genericResponse = new GenericResponse();
        try {

        } catch (Exception e) {
            elasticSearchUtils.pushException("VALIDATE_TOKEN", e.getMessage());
            genericResponse.setError(e.getMessage(), "Unable to validate you request, please try again!");
        }
        return ResponseEntity.ok(genericResponse);
    }

}
