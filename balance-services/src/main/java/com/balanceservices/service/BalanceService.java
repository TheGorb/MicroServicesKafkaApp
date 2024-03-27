package com.balanceservices.service;

import com.commonmessaging.model.Payment;
import com.commonmessaging.producer.CommonProducer;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {
    private final CommonProducer commonProducer;

    public BalanceService(CommonProducer commonProducer) {
        this.commonProducer = commonProducer;
    }

    public void addBalance(Payment payment) {
        commonProducer.sendKafkaEvent("addBalance", new Payment(String.valueOf(payment.getAmount()), "USD", payment.getCustomerId()).toJson(), "payment");
    }

    public void lowerBalance(Payment payment) {
        commonProducer.sendKafkaEvent("lowerBalance", new Payment(String.valueOf(payment.getAmount()), "USD", payment.getCustomerId()).toJson(), "payment");
    }
}
