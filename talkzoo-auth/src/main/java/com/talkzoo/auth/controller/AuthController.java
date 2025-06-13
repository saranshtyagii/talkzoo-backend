package com.talkzoo.auth.controller;

import com.talkzoo.auth.dto.GenericResponse;
import com.talkzoo.auth.payloads.RegisterUser;
import com.talkzoo.auth.payloads.UserCredentials;
import com.talkzoo.auth.security.JwtTokenUtils;
import com.talkzoo.auth.security.JwtUserDetailsServices;
import com.talkzoo.auth.services.AbstractServices.AuthenticationServices;
import com.web.kafka.elaslticsearch.ElasticSearchUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationServices authenticationServices;
    private final JwtTokenUtils jwtTokenUtils;
    private final JwtUserDetailsServices jwtUserDetailsServices;
    private final AuthenticationManager authenticationManager;
    private final ElasticSearchUtils elasticSearchUtils;

    public AuthController(AuthenticationServices authenticationServices, JwtTokenUtils jwtTokenUtils, JwtUserDetailsServices jwtUserDetailsServices, AuthenticationManager authenticationManager, ElasticSearchUtils elasticSearchUtils) {
        this.authenticationServices = authenticationServices;
        this.jwtTokenUtils = jwtTokenUtils;
        this.jwtUserDetailsServices = jwtUserDetailsServices;
        this.authenticationManager = authenticationManager;
        this.elasticSearchUtils = elasticSearchUtils;
    }

    @PostMapping("/register")
    public GenericResponse registerUser(@RequestBody  RegisterUser registerUser) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse.setResponse(authenticationServices.registerUser(registerUser));
        } catch (Exception e) {
            genericResponse.setError(registerUser, e.getMessage());
            elasticSearchUtils.pushException("REGISTER_USER", e.getMessage());
        }
        return genericResponse;
    }

    @PostMapping("/login")
    public GenericResponse authenticateUser(@RequestBody UserCredentials userCredentials) {
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

        } catch (Exception e) {
            genericResponse.setError(userCredentials, e.getMessage());
            elasticSearchUtils.pushException("LOGIN_USER", e.getMessage());
        }
        return genericResponse;
    }

}
