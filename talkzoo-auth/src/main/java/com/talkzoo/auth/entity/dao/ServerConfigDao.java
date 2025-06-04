package com.talkzoo.auth.entity.dao;

import com.talkzoo.auth.entity.ServerConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServerConfigDao extends MongoRepository<ServerConfig, String> {
}
