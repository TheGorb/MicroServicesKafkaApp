package com.commonmessaging.consumer;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CommonConsumer {
    @KafkaListener(topics = "customerEvents", groupId = "customerGroup")
    public void consumeCustomerEvent(String customerEvent) {
        System.out.println("Consumed the customer event from Kafka: " + customerEvent);
    }

    @KafkaListener(topics = "payment-events", groupId = "paymentGroup")
    public void consumePaymentEvent(String paymentEvent) {
        System.out.println("Consumed the payment event from Kafka: " + paymentEvent);
    }
}
