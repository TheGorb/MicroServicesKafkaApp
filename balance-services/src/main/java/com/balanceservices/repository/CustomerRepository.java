package com.balanceservices.repository;

import com.commonmessaging.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    Customer findByName(String name);
    Customer findByEmail(String email);
    Customer findByPhone(String phone);
}
