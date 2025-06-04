package com.talkzoo.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class TalkZooEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalkZooEurekaApplication.class, args);
	}

}
