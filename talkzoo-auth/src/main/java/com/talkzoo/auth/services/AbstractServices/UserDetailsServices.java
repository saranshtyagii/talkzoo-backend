package com.talkzoo.auth.services.AbstractServices;

import com.talkzoo.auth.entity.UserDocument;

public interface UserDetailsServices {

    UserDocument loadUserByUsername(String username);
    UserDocument loadUserByEmail(String email);

    boolean isUsernameExists(String username);
    boolean isEmailExists(String email);

    UserDocument loadUserByUsernameOrEmail(String usernameOrEmail);

}
