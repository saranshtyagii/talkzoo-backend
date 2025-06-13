package com.talkzoo.auth.services.ServicesBody;

import com.talkzoo.auth.entity.UserDocument;
import com.talkzoo.auth.entity.dao.UserMasterDao;
import com.talkzoo.auth.payloads.RegisterUser;
import com.talkzoo.auth.payloads.UserCredentials;
import com.talkzoo.auth.services.AbstractServices.AuthenticationServices;
import com.web.kafka.elaslticsearch.ElasticSearchUtils;
import com.web.kafka.utils.RedisUtils;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
@ComponentScan("com.web.kafka.*")
public class AuthServiceBody implements AuthenticationServices {

    private final UserMasterDao userMasterDao;
    private final RedisUtils redisUtils;
    private final PasswordEncoder passwordEncoder;
    private final ElasticSearchUtils elasticSearchUtils;

    public AuthServiceBody(UserMasterDao userMasterDao, RedisUtils redisUtils, PasswordEncoder passwordEncoder, ElasticSearchUtils elasticSearchUtils) {
        this.userMasterDao = userMasterDao;
        this.redisUtils = redisUtils;
        this.passwordEncoder = passwordEncoder;
        this.elasticSearchUtils = elasticSearchUtils;
    }


    @Override
    public String authenticateUser(UserCredentials userCredentials) {
        return "";
    }

    @Override
    public String registerUser(RegisterUser registerUser) {
        try {
            if(isUsernameExist(registerUser.getUsername())) {
                throw new Exception("Username is already is in use!");
            }

            if(!StringUtils.isEmpty(registerUser.getEmail())
                && isEmailExists(registerUser.getEmail())) {
                throw new Exception("Email is already exists!");
            }

            UserDocument userDocument = new UserDocument();
            userDocument.setUsername(registerUser.getUsername());
            if(!StringUtils.isEmpty(registerUser.getEmail())) {
                userDocument.setEmail(registerUser.getEmail());
            }
            if(!StringUtils.isEmpty(registerUser.getFullName())) {
                userDocument.setFullName(registerUser.getFullName());
            }

            userDocument.setPassword(passwordEncoder.encode(registerUser.getPassword()));
            userDocument.setUserVerified(false);
            userDocument.setUserDeleted(false);
            UserDocument savedUser = userMasterDao.save(userDocument);
            if(ObjectUtils.isEmpty(savedUser)) {
                throw new Exception("Failed to create user!");
            }
            return savedUser.getUsername();
        } catch (Exception e) {
            elasticSearchUtils.pushException("REGISTER_USER_EXCEPTION", e.getMessage());
        }
        return null;
    }


    private boolean isUsernameExist(String username) {
        try {
            // check username available is redis for active members
            Object usernameInRedis = redisUtils.get(username);
            if(ObjectUtils.isEmpty(usernameInRedis)) {
                if(userMasterDao.existsByUsername(username)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isEmailExists(String email) {
        try {
            Object emailInRedis = redisUtils.get(email);
            if(ObjectUtils.isEmpty(emailInRedis)) {
                if(userMasterDao.existsByEmail(email)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
