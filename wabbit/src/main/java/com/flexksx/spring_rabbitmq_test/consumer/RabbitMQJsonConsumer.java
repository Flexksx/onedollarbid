package com.flexksx.spring_rabbitmq_test.consumer;

import com.flexksx.spring_rabbitmq_test.dto.AuctionItem;
import com.flexksx.spring_rabbitmq_test.publisher.Sender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQJsonConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQJsonConsumer.class);
    private final Sender sender;

    @RabbitListener(queues = { "${rabbitmq.queue.name}" })
    public void consume(AuctionItem item) {
        LOGGER.info(String.format("Received message from rabbitmq queue: %s", item.toString()));

        sender.sendAuctionItemPostRequest(item);
    }
}
