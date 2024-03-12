package com.customermanagementservices.consumer;

import com.commonmessaging.model.Customer;
import com.commonmessaging.producer.CommonProducer;
import com.customermanagementservices.repository.CustomerRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CustomerConsumer {
    private final CustomerRepository customerRepository;
    private final CommonProducer commonProducer;

    public CustomerConsumer(CustomerRepository customerRepository, CommonProducer commonProducer) {
        this.customerRepository = customerRepository;
        this.commonProducer = commonProducer;
    }

    @KafkaListener(topics = "addCustomer", groupId = "customerGroup", concurrency = "1")
    public void addCustomer(Customer customerEvent) {
        Customer newCustomer = customerRepository.save(customerEvent);
        commonProducer.sendKafkaEvent("newCustomer", newCustomer.toJson());
    }

    @KafkaListener(topics = "updateCustomer", groupId = "customerGroup")
    public void updateCustomer(Customer customerEvent) {
        customerRepository.findById(customerEvent.getId())
                .ifPresent(customer -> {
                    customer.setName(customerEvent.getName());
                    customer.setEmail(customerEvent.getEmail());
                    customer.setPhone(customerEvent.getPhone());
                    customerRepository.save(customer);
        });
    }
}
