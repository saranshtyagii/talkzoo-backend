package com.web.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class NotificationQueueApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationQueueApplication.class, args);
	}

}
