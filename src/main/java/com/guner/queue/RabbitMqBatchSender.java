package com.guner.queue;

import com.guner.model.ChargingRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RabbitMqBatchSender {

    private final RabbitTemplate rabbitTemplate;
    private final BatchingRabbitTemplate batchingRabbitTemplate;

    @Value("${batch-sender.topic-exchange.name}")
    private String topicExchange;

    @Value("${batch-sender.routing.key.batch-routing}")
    private String routingKeyBatch;

    public void messageSend(ChargingRecord chargingRecord) {
        rabbitTemplate.convertAndSend(topicExchange, routingKeyBatch, chargingRecord);
    }

    public void messageBatchSend(ChargingRecord chargingRecord) {
        IntStream.range(0,101).forEach(i ->
                batchingRabbitTemplate.convertAndSend(topicExchange, routingKeyBatch, chargingRecord));
        batchingRabbitTemplate.flush();;


    }
}
