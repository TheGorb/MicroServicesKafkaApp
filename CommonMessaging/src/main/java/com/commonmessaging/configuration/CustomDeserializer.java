package com.commonmessaging.configuration;

import com.commonmessaging.model.Balance;
import com.commonmessaging.model.Customer;
import com.commonmessaging.model.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.apache.kafka.common.serialization.Deserializer;

public class CustomDeserializer implements Deserializer<Object> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object deserialize(String s, byte[] bytes) {
        String json = new String(bytes);
        System.out.println("json=" + json);
        String replace = json.substring(1, json.length() - 1).replace("\\", "");
        System.out.println("replace=" + replace);
        JSONObject jsonObject = new JSONObject(replace);
        String type = jsonObject.getString("type");

        try {
            JsonNode rootNode = objectMapper.readTree(replace);
            return determineClassFromTypeIndicator(type, rootNode.path("event"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    private Object determineClassFromTypeIndicator(String typeIndicator, JsonNode node) throws JsonProcessingException {
        return switch (typeIndicator) {
            case "payment" -> objectMapper.treeToValue(node, Payment.class);
            case "balance" -> objectMapper.treeToValue(node, Balance.class);
            case "customer" -> objectMapper.treeToValue(node, Customer.class);
            default -> throw new RuntimeException("Unknown type: " + typeIndicator);
        };
    }
}
