package com.tadiwanashe.wms.messaging;

import com.tadiwanashe.wms.entity.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaOrderEventPublisher {

    private static final String TOPIC = "order.created";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaOrderEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreated(Order order) {
        String message = String.format(
                "{\"orderId\":%d,\"customerId\":\"%s\",\"status\":\"%s\"}",
                order.getId(),
                order.getCustomerId(),
                order.getStatus()
        );
        kafkaTemplate.send(TOPIC, String.valueOf(order.getId()), message);
    }
}