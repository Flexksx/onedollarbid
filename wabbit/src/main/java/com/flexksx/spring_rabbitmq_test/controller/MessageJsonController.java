package com.flexksx.spring_rabbitmq_test.controller;

import com.flexksx.spring_rabbitmq_test.dto.AuctionItem;
import com.flexksx.spring_rabbitmq_test.publisher.RabbitMQJsonProducer;
import com.flexksx.spring_rabbitmq_test.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/json")
@RequiredArgsConstructor
public class MessageJsonController {

    private final RabbitMQJsonProducer jsonProducer;

    @PostMapping("/publish")
    public ResponseEntity<ApiResponse> sendJsonMessage(@RequestBody AuctionItem item) {
        jsonProducer.sendJsonMessage(item);
        return ResponseEntity.ok(new ApiResponse("Success", item));
    }
}
