package com.commonmessaging.producer;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommonProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CommonProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendKafkaEvent(String topic, Object event, String eventType) throws RuntimeException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("type", eventType);
        jsonObject.put("event", event);

        log.debug("Sending Kafka event. Topic: {}, EventType: {}, Payload: {}", topic, eventType, jsonObject);

        kafkaTemplate.send(topic, jsonObject.toString());
    }
}
