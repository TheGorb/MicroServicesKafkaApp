package com.balanceservices.repository;

import com.commonmessaging.model.Balance;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BalanceRepository extends MongoRepository<Balance, String> {
    Balance findByCustomerId(String customerId);
}
