package com.guner.controller;

import com.guner.service.BatchProcessorService;
import com.guner.model.MessageBody;
import com.guner.service.SingleProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BatchProcessorController {

    private final BatchProcessorService batchProcessorService;
    private final SingleProcessorService singleProcessorService;

    @PostMapping("/sendBatchMessage")
    public boolean sendBatchMessage(@RequestBody MessageBody messageBody) {
        return batchProcessorService.process(messageBody);
    }

    @PostMapping("/sendSingleMessage")
    public boolean sendSingleMessage(@RequestBody MessageBody messageBody) {
        return singleProcessorService.process(messageBody);
    }
}