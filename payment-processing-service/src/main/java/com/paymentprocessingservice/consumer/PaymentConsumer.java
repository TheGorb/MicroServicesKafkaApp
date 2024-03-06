package com.paymentprocessingservice.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {
    @KafkaListener(topics = "addCustomer", groupId = "customerGroup", concurrency = "1")
    public void consumePaymentEvent(String newCustomerEvent) {
        System.out.println("a new customer has been created: " + newCustomerEvent);
    }
}
