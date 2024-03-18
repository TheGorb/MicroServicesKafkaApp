package com.commonmessaging.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Payment {
    @Id
    @JsonProperty("id")
    private String id;

    private final String amount;
    private final String currency;
    private final String customerId;

    @JsonCreator
    public Payment(@JsonProperty("amount") String amount,@JsonProperty("currency") String currency,@JsonProperty("customerId") String customerId) {
        this.amount = amount;
        this.currency = currency;
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAmount() {
        return amount;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("amount", amount);
        json.put("currency", currency);
        json.put("customerId", currency);
        return json;
    }

    public String getCustomerId() {
        return customerId;
    }
}
