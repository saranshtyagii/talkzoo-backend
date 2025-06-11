package com.talkzoo.auth.services.ServicesBody;

import com.talkzoo.auth.entity.UserDocument;
import com.talkzoo.auth.services.AbstractServices.UserDetailsServices;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceBody implements UserDetailsServices {
    @Override
    public UserDocument loadUserByUsername(String username) {
        return null;
    }

    @Override
    public UserDocument loadUserByEmail(String email) {
        return null;
    }

    @Override
    public boolean isUsernameExists(String username) {
        return false;
    }

    @Override
    public boolean isEmailExists(String email) {
        return false;
    }

    @Override
    public UserDocument loadUserByUsernameOrEmail(String usernameOrEmail) {
        return null;
    }
}
