package com.paymentprocessingservice.repository;

import com.commonmessaging.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    Payment findByCustomerId(String customerId);
}
