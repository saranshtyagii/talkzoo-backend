package com.talkzoo.auth.services;

import com.talkzoo.auth.entity.ServerConfig;
import com.talkzoo.auth.entity.dao.ServerConfigDao;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class ServerConfigService {

    private static ServerConfig  serverConfig;

    private final ServerConfigDao serverConfigDao;

    public ServerConfigService(ServerConfigDao serverConfigDao) {
        this.serverConfigDao = serverConfigDao;
    }

    public void fetchServerConfigFromDB() {
        try {
            List<ServerConfig> serverConfigs = serverConfigDao.findAll();
            if(!CollectionUtils.isEmpty(serverConfigs)) {
                serverConfig = serverConfigs.get(0);
            } else {
                createAndSaveServerConfig();
            }
        } catch (Exception e) {
            System.err.println("Config can't be loaded: "+e.getMessage());
        }
        System.err.println("Config loaded successfully.");
    }

    public void refreshServerConfig() {
        fetchServerConfigFromDB();
    }


    private ServerConfig createAndSaveServerConfig() {
        System.err.println("Create new ServerConfig");
        ServerConfig serverConfig = ServerConfig.builder()
                .jwtSecretKey("JHRhbGt6b28mbmV4bG9naWNzIzcwMTFAOTQxMg==")
                .jwtIssuer("TalkZoo_NexLogics")
                .apiKey("bangur@9470")
                .prod(false)
                .build();
        return serverConfigDao.save(serverConfig);
    }

    public static ServerConfig getServerConfig() {
        if(ObjectUtils.isEmpty(serverConfig)) {
            throw new RuntimeException("ServerConfig is empty");
        }
        return serverConfig;
    }

}
