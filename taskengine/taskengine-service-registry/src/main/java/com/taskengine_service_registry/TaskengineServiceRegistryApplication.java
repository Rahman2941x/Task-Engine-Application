package com.taskengine_service_registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class TaskengineServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskengineServiceRegistryApplication.class, args);
	}

}
