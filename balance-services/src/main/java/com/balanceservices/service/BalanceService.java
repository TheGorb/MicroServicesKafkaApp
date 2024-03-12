package com.balanceservices.service;

import com.balanceservices.repository.BalanceRepository;
import com.commonmessaging.model.Balance;
import com.commonmessaging.model.Payment;
import com.commonmessaging.producer.CommonProducer;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {
    private final BalanceRepository balanceRepository;
    private final CommonProducer commonProducer;

    public BalanceService(BalanceRepository balanceRepository, CommonProducer commonProducer) {
        this.balanceRepository = balanceRepository;
        this.commonProducer = commonProducer;
    }

    public void addBalance(String customerId, double amount) {
        commonProducer.sendKafkaEvent("addBalance", new Payment(String.valueOf(amount), "USD", customerId).toJson());
    }

    public void lowerBalance(String customerId, double amount) {
        commonProducer.sendKafkaEvent("lowerBalance", new Payment(String.valueOf(amount), "USD", customerId).toJson());
    }
}
