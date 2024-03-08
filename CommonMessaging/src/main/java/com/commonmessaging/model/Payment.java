package com.commonmessaging.model;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Payment {
    @Id
    private String id;

    private final String customerName;
    private final String amount;
    private final String currency;

    public Payment(String customerName, String amount, String currency) {
        this.customerName = customerName;
        this.amount = amount;
        this.currency = currency;
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

    public String getCustomerName() {
        return customerName;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("customerName", customerName);
        json.put("amount", amount);
        json.put("currency", currency);
        return json;
    }
}
