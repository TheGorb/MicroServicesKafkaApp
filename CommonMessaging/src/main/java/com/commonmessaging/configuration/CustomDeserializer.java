package com.commonmessaging.configuration;

import com.commonmessaging.model.Payment;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.io.InputStream;

public class CustomDeserializer implements Deserializer<Object> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object deserialize(String s, byte[] bytes) {
        try {
            JsonNode node = objectMapper.readTree(bytes);
            Class<?> targetClass;

            throw new RuntimeException(node.asText());
/*            System.out.println(node);
            if (node.has("typeFieldOrIndicator")) {
                String typeIndicator = node.get("typeFieldOrIndicator").asText();
                // Determine the class to deserialize to based on the type indicator
                targetClass = determineClassFromTypeIndicator(typeIndicator);
            } else {
                // Default class if no type indicator is found
                targetClass = Payment.class;
            }*/
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //return null;
    }

    private Class<?> determineClassFromTypeIndicator(String typeIndicator) {
        return null; // Placeholder
    }
}
