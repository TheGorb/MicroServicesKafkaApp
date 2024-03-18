package com.commonmessaging.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Customer {
    @Getter
    @Id
    @JsonProperty("id")
    private String id;

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String phone;
    @Setter
    @JsonProperty("customerPayment")
    private ArrayList<Payment> customerPayment = new ArrayList<>();
    @Setter
    @Getter
    @JsonProperty("customerBalance")
    private Balance customerBalance;

    @JsonCreator
    public Customer(@JsonProperty("name") String name, @JsonProperty("email") String email, @JsonProperty("phone") String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public void addPayment(Payment payment) {
        this.customerPayment.add(payment);
    }

    public List<Payment> getCustomerPayment() {
        return this.customerPayment;
    }

    public void removePayment(Payment payment) {
        this.customerPayment.remove(payment);
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

