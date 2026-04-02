package com.taskengine.taskengine_task_service.utils;

import com.taskengine.taskengine_task_service.configuration.RabbitMqPropertiesConfig;
import com.taskengine.taskengine_task_service.dto.ProjectTaskDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MqTaskProducerUtil {

    @Value("${RABBITMQ_EXCHANGE}")
    private String exchange;

    @Value("${RABBITMQ_PROJECT_TASK_QUEUE}")
    private String queue;

    @Value("${RABBITMQ_ROUTING_KEY}")
    private String routingKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMqPropertiesConfig rabbitProps;

    public void sendTaskUpdate(ProjectTaskDTO projectTaskDTO){
        rabbitTemplate.convertAndSend(exchange,rabbitProps.getRoutingKey(),projectTaskDTO);
    }
}
