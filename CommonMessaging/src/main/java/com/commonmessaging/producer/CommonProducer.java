package com.commonmessaging.producer;

import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommonProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CommonProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendKafkaEvent(String topic, Object event, String eventType) throws RuntimeException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("type", eventType);
        jsonObject.put("event", event);
        System.out.println("Sending event: " + event);
        kafkaTemplate.send(topic, jsonObject.toString());
    }
}
