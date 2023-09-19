package com.guner.controller;

import com.guner.service.BatchProcessorService;
import com.guner.model.MessageBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BatchProcessorController {

    private final BatchProcessorService batchProcessorService;

    @PostMapping("/sendMessage")
    public boolean sendMessage(@RequestBody MessageBody messageBody) {
        return batchProcessorService.process(messageBody);
    }
}