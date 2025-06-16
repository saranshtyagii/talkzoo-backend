package com.talkzoo.auth.controller;

import com.talkzoo.auth.dto.GenericResponse;
import com.talkzoo.auth.payloads.DeviceDetails;
import com.talkzoo.auth.payloads.RegisterUser;
import com.talkzoo.auth.payloads.UserCredentials;
import com.talkzoo.auth.security.JwtTokenUtils;
import com.talkzoo.auth.security.JwtUserDetailsServices;
import com.talkzoo.auth.services.AbstractServices.AuthenticationServices;
import com.web.kafka.elaslticsearch.ElasticSearchUtils;
import com.web.kafka.helper.LogsEvents;
import com.web.kafka.helper.MapperUtils;
import com.web.kafka.utils.RedisUtils;
import org.apache.catalina.mapper.Mapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public AuthController(AuthenticationServices authenticationServices, JwtTokenUtils jwtTokenUtils, JwtUserDetailsServices jwtUserDetailsServices, AuthenticationManager authenticationManager, ElasticSearchUtils elasticSearchUtils, RedisUtils redisUtils) {
        this.authenticationServices = authenticationServices;
        this.jwtTokenUtils = jwtTokenUtils;
        this.jwtUserDetailsServices = jwtUserDetailsServices;
        this.authenticationManager = authenticationManager;
        this.elasticSearchUtils = elasticSearchUtils;
        this.redisUtils = redisUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponse> registerUser(@RequestBody  RegisterUser registerUser) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse.setResponse(authenticationServices.registerUser(registerUser));
        } catch (Exception e) {
            genericResponse.setError(registerUser, e.getMessage());
            elasticSearchUtils.pushException("REGISTER_USER", e.getMessage());
        }
        return ResponseEntity.ok(genericResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse> authenticateUser(@RequestBody UserCredentials userCredentials) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword())
                );
            } catch (Exception e) {
                throw new Exception("Invalid username or password");
            }

            UserDetails userDetails = jwtUserDetailsServices.loadUserByUsername(userCredentials.getUsername());
            String token = jwtTokenUtils.generateToken(userDetails);
            genericResponse.setResponse(token);
            elasticSearchUtils.push(LogsEvents.builder()
                            .username(userDetails.getUsername())
                            .message("LOGIN_SUCCESS")
                    .build());
        } catch (Exception e) {
            genericResponse.setError(userCredentials, e.getMessage());
            elasticSearchUtils.pushException("LOGIN_USER", e.getMessage());
        }
        return ResponseEntity.ok(genericResponse);
    }

    public ResponseEntity<GenericResponse> authenticateGuestUser(@RequestBody DeviceDetails deviceDetails) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            String key = deviceDetails.getDeviceId();
            String value = UUID.randomUUID() + "_" + MapperUtils.convertObjectToString(deviceDetails);
            redisUtils.set(key, value);
        } catch (Exception e) {
            elasticSearchUtils.pushException("AUTHENTICATE_GUEST_USER", e.getMessage());
        }
        return ResponseEntity.ok(genericResponse);
    }


    public ResponseEntity<GenericResponse> updateTokenInRedis(String token) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            if(jwtTokenUtils.isTokenExpired(token)) {
                genericResponse.setError("Token is expired");
                return new ResponseEntity<>(genericResponse, HttpStatus.UNAUTHORIZED);
            }
            String savedToken = redisUtils.get(token);
            if(StringUtils.isNotBlank((savedToken))) {
                redisUtils.set(token, savedToken, RedisUtils.FIFTEEN_MINUTES);
            }
            genericResponse.setResponse(token, "Token successfully updated");
        } catch (Exception e) {
            elasticSearchUtils.pushException("UPDATE_TOKEN_IN_REDIS", e.getMessage());
        }
        return ResponseEntity.ok(genericResponse);
    }

}
