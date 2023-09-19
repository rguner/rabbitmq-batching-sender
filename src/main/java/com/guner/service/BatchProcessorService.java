package com.guner.service;

import com.guner.model.ChargingRecord;
import com.guner.model.MessageBody;
import com.guner.queue.RabbitMqBatchSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class BatchProcessorService {

    private final RabbitMqBatchSender rabbitMqBatchSender;
    public boolean process(MessageBody messageBody) {

        if (messageBody.getTransactionDate() == null ) {
            messageBody.setTransactionDate(LocalDateTime.now());
        }
        ChargingRecord chargingRecord = ChargingRecord.builder()
                .sourceGsm(messageBody.getSourceGsm())
                .targetGsm(messageBody.getTargetGsm())
                .transactionDate(java.util.Date
                        .from(messageBody.getTransactionDate().atZone(ZoneId.systemDefault())
                                .toInstant()))
                .build();

        // single message sender
        //rabbitMqBatchSender.messageSend(chargingRecord);

        // batch sender
        rabbitMqBatchSender.messageBatchSend(chargingRecord);

        return true;
    }
}

