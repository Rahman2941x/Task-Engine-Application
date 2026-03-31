package com.taskengine.taskengine_task_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TaskengineTaskServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskengineTaskServiceApplication.class, args);
	}

}
