package taskengine.taskengine_project_service.utils;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import taskengine.taskengine_project_service.configuration.RabbitMqProperties;
import taskengine.taskengine_project_service.dto.ProjectTaskDTO;

@Component
public class MqProjectProducerUtil {


    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqProperties rabbitMqProperties;

    public MqProjectProducerUtil(RabbitTemplate rabbitTemplate, RabbitMqProperties rabbitMqProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMqProperties = rabbitMqProperties;
    }

    public void sendProjectToTask(ProjectTaskDTO projectTaskDTO){
        rabbitTemplate.convertAndSend(
                rabbitMqProperties.getExchange(),
                rabbitMqProperties.getProjectTask().getRoutingKey(),
                projectTaskDTO
        );
    }

}
