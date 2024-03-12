package com.commonmessaging.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.json.JsonObject;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Customer {
    @Id
    @JsonProperty("id")
    private String id;

    private String name;
    private String email;
    private String phone;
    @JsonProperty("customerPayment")
    private ArrayList<Payment> customerPayment = new ArrayList<>();
    @JsonProperty("customerBalance")
    private Balance customerBalance;

    @JsonCreator
    public Customer(@JsonProperty("name") String name, @JsonProperty("email") String email, @JsonProperty("phone") String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCustomerPayment(ArrayList<Payment> customerPayment) {
        this.customerPayment = customerPayment;
    }

    public void addPayment(Payment payment) {
        this.customerPayment.add(payment);
    }

    public List<Payment> getCustomerPayment() {
        return customerPayment;
    }

    public void removePayment(Payment payment) {
        this.customerPayment.remove(payment);
    }

    public void setCustomerBalance(Balance customerBalance) {
        this.customerBalance = customerBalance;
    }

    public Balance getCustomerBalance() {
        return customerBalance;
    }

    @Override
    public String toString() {
        return "Customer {" +
                "\n\tid=" + id +
                ",\n\tname=" + name +
                ",\n\temail=" + email +
                ",\n\tphone=" + phone +
                ",\n\tpayment=" + customerPayment +
                ",\n\tbalance=" + customerBalance +
                "\n}";
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("email", email);
        json.put("phone", phone);
        return json;
    }
}

