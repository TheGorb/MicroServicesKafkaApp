package com.customermanagementservices.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CustomerConsumer {
    @KafkaListener(topics = "addCustomer", groupId = "customerGroup")
    public void addCustomer(String customerEvent) {
        System.out.println("Consumed the customer event from Kafka: " + customerEvent);
    }

    @KafkaListener(topics = "updateCustomer", groupId = "customerGroup")
    public void updateCustomer(String customerEvent) {
        System.out.println("Consumed the customer event from Kafka: " + customerEvent);
    }
}
