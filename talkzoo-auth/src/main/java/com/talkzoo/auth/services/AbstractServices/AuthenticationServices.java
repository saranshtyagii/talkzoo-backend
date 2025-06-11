package com.talkzoo.auth.services.AbstractServices;

import com.talkzoo.auth.dto.GenericResponse;
import com.talkzoo.auth.payloads.RegisterUser;
import com.talkzoo.auth.payloads.UserCredentials;

public interface AuthenticationServices {

    String authenticateUser(UserCredentials userCredentials);
    String registerUser(RegisterUser registerUser);

}
