package com.taskengine.taskengine_task_service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMqProperties {

    private String exchange;
    private QueueConfig ProjectTask;
    private QueueConfig taskProject;

    public static class QueueConfig {
        private String queue;
        private String routingKey;

        public String getQueue() {
            return queue;
        }

        public void setQueue(String queue) {
            this.queue = queue;
        }

        public String getRoutingKey() {
            return routingKey;
        }

        public void setRoutingKey(String routingKey) {
            this.routingKey = routingKey;
        }
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public QueueConfig getProjectTask() {
        return ProjectTask;
    }

    public void setProjectTask(QueueConfig projectTask) {
        ProjectTask = projectTask;
    }

    public QueueConfig getTaskProject() {
        return taskProject;
    }

    public void setTaskProject(QueueConfig taskProject) {
        this.taskProject = taskProject;
    }
}
