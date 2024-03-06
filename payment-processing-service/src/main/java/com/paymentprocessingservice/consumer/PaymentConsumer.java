package com.paymentprocessingservice.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {
    @KafkaListener(topics = "payment-events", groupId = "paymentGroup")
    public void consumePaymentEvent(String paymentEvent) {
        System.out.println("Consumed the payment event from Kafka: " + paymentEvent);
    }
}
