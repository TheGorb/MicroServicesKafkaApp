package com.commonmessaging.producer;

import com.commonmessaging.model.Customer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class CommonProducer {

    private final KafkaTemplate<String, Customer> kafkaTemplate;

    public CommonProducer(KafkaTemplate<String, Customer> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendKafkaEvent(String topic, Customer event) {
        kafkaTemplate.send(topic, event);
        System.out.println("Published the event " + topic + " to Kafka: " + event);
    }
}
