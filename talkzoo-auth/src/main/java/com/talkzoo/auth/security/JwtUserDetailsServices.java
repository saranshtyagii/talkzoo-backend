package com.talkzoo.auth.security;

import com.talkzoo.auth.entity.UserDocument;
import com.talkzoo.auth.entity.dao.UserMasterDao;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;

@Service
public class JwtUserDetailsServices implements UserDetailsService {

    private final UserMasterDao userMasterDao;

    public JwtUserDetailsServices(UserMasterDao userMasterDao) {
        this.userMasterDao = userMasterDao;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDocument savedUser = userMasterDao.findByUsername(username);
        if(ObjectUtils.isEmpty(savedUser)) {
            throw new UsernameNotFoundException("Username *"+username+"* not found");
        }
        return new User(
                savedUser.getUsername(),
                savedUser.getPassword(),
                Collections.emptyList()
        );
    }
}
