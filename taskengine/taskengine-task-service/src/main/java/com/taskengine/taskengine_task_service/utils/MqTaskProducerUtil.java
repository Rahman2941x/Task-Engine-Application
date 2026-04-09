package com.taskengine.taskengine_task_service.utils;

import com.taskengine.taskengine_task_service.configuration.RabbitMqProperties;
import com.taskengine.taskengine_task_service.dto.ProjectTaskDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MqTaskProducerUtil {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqProperties rabbitMqProperties;

    public MqTaskProducerUtil(RabbitTemplate rabbitTemplate, RabbitMqProperties rabbitMqProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMqProperties = rabbitMqProperties;
    }

    public void sendTaskToProject(ProjectTaskDTO projectTaskDTO){
        rabbitTemplate.convertAndSend(
                rabbitMqProperties.getExchange(),
                rabbitMqProperties.getTaskProject().getRoutingKey(),
                projectTaskDTO);
    }

    public void sendProjectToTask(ProjectTaskDTO projectTaskDTO) {
        rabbitTemplate.convertAndSend(
                rabbitMqProperties.getExchange(),
                rabbitMqProperties.getProjectTask().getRoutingKey(),
                projectTaskDTO
        );
    }
}
