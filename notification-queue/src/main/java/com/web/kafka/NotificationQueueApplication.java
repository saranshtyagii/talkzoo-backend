package com.web.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NotificationQueueApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationQueueApplication.class, args);
	}

}
