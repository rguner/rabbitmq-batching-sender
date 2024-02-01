package com.guner.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.batch.BatchingStrategy;
import org.springframework.amqp.rabbit.batch.SimpleBatchingStrategy;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@Configuration
public class RabbitMqConfiguration {

    @Value("${batch-sender.topic-exchange.name}")
    private String topicExchange;

    @Value("${batch-sender.queue.name.batch-queue}")
    private String queueBatch;

    @Value("${batch-sender.routing.key.batch-routing}")
    private String routingKeyBatch;

    @Bean
    public Queue queueBatch() {
        return new Queue(queueBatch);
    }


    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(topicExchange);
    }

    @Bean
    public Binding bindingBatch() {
        return BindingBuilder
                .bind(queueBatch())
                .to(topicExchange())
                .with(routingKeyBatch);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    // bu bean varsa normal gonderimler de batch olarak gidiyor single gönderim için comment out yap
    @Bean
    public BatchingRabbitTemplate batchingRabbitTemplate(ConnectionFactory connectionFactory) {
        //BatchingStrategy strategy = new SimpleBatchingStrategy(500, 25_000, 3_000);
        BatchingStrategy strategy = new SimpleBatchingStrategy(5, 25_000, 20_000);
        TaskScheduler scheduler = new ConcurrentTaskScheduler();
        BatchingRabbitTemplate batchingRabbitTemplate = new BatchingRabbitTemplate(connectionFactory, strategy, scheduler);
        batchingRabbitTemplate.setMessageConverter(converter());
        return batchingRabbitTemplate;
    }
}
