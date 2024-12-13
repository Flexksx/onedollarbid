package com.flexksx.spring_rabbitmq_test.publisher;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.flexksx.spring_rabbitmq_test.dto.AuctionItem;

@Service
@RequiredArgsConstructor
public class RabbitMQJsonProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingJsonKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQJsonProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public void sendJsonMessage(AuctionItem item) {
        LOGGER.info("Sending message to exchange: {}", item.toString());
        rabbitTemplate.convertAndSend(exchange, routingJsonKey, item);
    }
}
