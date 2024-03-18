package com.commonmessaging.configuration;

import com.commonmessaging.model.Balance;
import com.commonmessaging.model.Customer;
import com.commonmessaging.model.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class CustomDeserializer implements Deserializer<Object> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object deserialize(String s, byte[] bytes) {
        String json = new String(bytes);
        log.debug("Deserializing JSON: {}", json);

        String replace = json.substring(1, json.length() - 1).replace("\\", "");
        JSONObject jsonObject = new JSONObject(replace);
        String type = jsonObject.getString("type");
        log.debug("Detected type indicator: {}", type);

        try {
            JsonNode rootNode = objectMapper.readTree(replace);
            log.info("Successfully deserialized type '{}'.", type);
            return determineClassFromTypeIndicator(type, rootNode.path("event"));
        } catch (JsonProcessingException e) {
            log.error("Error deserializing JSON: {}", replace, e);
            throw new RuntimeException(e);
        }
    }

    private Object determineClassFromTypeIndicator(String typeIndicator, JsonNode node) throws JsonProcessingException {
        try {
            return switch (typeIndicator) {
                case "payment" -> objectMapper.treeToValue(node, Payment.class);
                case "balance" -> objectMapper.treeToValue(node, Balance.class);
                case "customer" -> objectMapper.treeToValue(node, Customer.class);
                default -> throw new RuntimeException("Unknown type: " + typeIndicator);
            };
        } catch (RuntimeException e) {
            log.error("Failed to determine class from type indicator '{}'.", typeIndicator, e);
            throw e;
        }
    }
}
