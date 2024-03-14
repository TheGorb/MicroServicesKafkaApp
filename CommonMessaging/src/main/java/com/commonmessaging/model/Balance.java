package com.commonmessaging.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Balance {
    @Id
    @JsonProperty("id")
    private String id;

    private final String customerId;
    private String currentBalance;
    private String pendingCharges;

    public Balance(@JsonProperty("customerId") String customerId, @JsonProperty("currentBalance") String currentBalance, @JsonProperty("pendingCharges") String pendingCharges) {
        this.customerId = customerId;
        this.currentBalance = currentBalance;
        this.pendingCharges = pendingCharges;
    }

    public String getPendingCharges() {
        return pendingCharges;
    }

    public String getBalance() {
        return currentBalance;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getId() {
        return id;
    }

    public void addBalance(String amount) {
        this.currentBalance = String.valueOf(Double.parseDouble(this.currentBalance) + Double.parseDouble(amount));
    }

    public void addPendingCharges(String amount) {
        this.pendingCharges = String.valueOf(Double.parseDouble(this.pendingCharges) + Double.parseDouble(amount));
    }

    public void lowerBalance(String amount) {
        this.currentBalance = String.valueOf(Double.parseDouble(this.currentBalance) - Double.parseDouble(amount));
    }

    public void lowerPendingCharges(String amount) {
        this.pendingCharges = String.valueOf(Double.parseDouble(this.pendingCharges) - Double.parseDouble(amount));
    }

    @Override
    public String toString() {
        return "Customer {" +
                "\n\tid=" + id +
                ",\n\tcustomerId=" + customerId +
                ",\n\tcurrentBalance=" + currentBalance +
                ",\n\tpendingCharges=" + pendingCharges +
                "\n}";
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("customerId", customerId);
        json.put("currentBalance", currentBalance);
        json.put("pendingCharges", pendingCharges);
        return json;
    }
}
