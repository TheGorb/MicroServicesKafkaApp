package com.commonmessaging.producer;

import com.commonmessaging.model.Customer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommonProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CommonProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendKafkaEvent(String topic, Object event) {
        kafkaTemplate.send(topic, event);
        System.out.println("Published the event " + topic + " to Kafka: " + event);
    }
}
