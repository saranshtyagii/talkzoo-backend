package com.talkzoo.auth;

import com.talkzoo.auth.services.ServerConfigService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoAuditing
public class TalkZooAuthApplication implements ApplicationListener<ApplicationReadyEvent> {

	private final ServerConfigService configService;

    public TalkZooAuthApplication(ServerConfigService configService) {
        this.configService = configService;
    }

    public static void main(String[] args) {
		SpringApplication.run(TalkZooAuthApplication.class, args);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		configService.fetchServerConfigFromDB();
	}
}
