package com.talkzoo.auth.entity.dao;

import com.talkzoo.auth.entity.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserMasterDao extends MongoRepository<UserDocument, String> {
    UserDocument findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
