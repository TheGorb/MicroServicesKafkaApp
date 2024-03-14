package com.commonmessaging.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommonProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    public CommonProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendKafkaEvent(String topic, Object event, String eventType) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", eventType);
            jsonObject.put("event", event);
            System.out.println("Sending event: " + event);
            kafkaTemplate.send(topic, jsonObject.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
