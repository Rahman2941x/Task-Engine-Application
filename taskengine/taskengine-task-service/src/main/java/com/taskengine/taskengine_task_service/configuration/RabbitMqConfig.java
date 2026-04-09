package com.taskengine.taskengine_task_service.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    RabbitMqProperties rabbitMqProps;

    public RabbitMqConfig(RabbitMqProperties rabbitMqProps) {
        this.rabbitMqProps=rabbitMqProps;
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(rabbitMqProps.getExchange());
    }


    //  Project → Task

    @Bean
    public Queue projectTaskQueue(){
        Map<String,Object> args= new HashMap<>();
        args.put("x-dead-letter-exchange", "dlx.exchange");
        args.put("x-dead-letter-routing-key", "dlx.routing");

        return new Queue(
                rabbitMqProps.getProjectTask().getQueue(),
                true,
                false,
                false,
                args
        );
    }

    @Bean
    public Binding projectTaskBinding(
            @Qualifier("projectTaskQueue")  Queue projectTaskQueue, Exchange exchange){
        return BindingBuilder.bind(projectTaskQueue)
                .to(exchange)
                .with(rabbitMqProps.getProjectTask().getRoutingKey())
                .noargs();
    }

    // Task → Project

    @Bean
    public  Queue taskProjectQueue(){
        Map<String,Object> args= new HashMap<>();
        args.put("x-dead-letter-exchange", "dlx.exchange");
        args.put("x-dead-letter-routing-key", "dlx.routing");

        return new Queue(
                rabbitMqProps.getTaskProject().getQueue(),
                true,
                false,
                false,
                args
        );
    }

    @Bean
    public  Binding taskProjectBinding(@Qualifier("taskProjectQueue") Queue taskProjectQueue,
                                       Exchange exchange){
        return BindingBuilder.bind(taskProjectQueue)
                .to(exchange)
                .with(rabbitMqProps.getTaskProject().getRoutingKey())
                .noargs();
    }

    @Bean
    public  Queue deadLetterQueue(){
        return new Queue("dlq.queue");
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange("dlx.exchange");
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("dlx.routing");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory,
                                         MessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }



}

