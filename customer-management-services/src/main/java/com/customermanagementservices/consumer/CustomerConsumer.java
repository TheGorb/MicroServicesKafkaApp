package com.customermanagementservices.consumer;

import com.commonmessaging.model.Customer;
import com.commonmessaging.producer.CommonProducer;
import com.commonmessaging.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerConsumer {
    private final CustomerRepository customerRepository;
    private final CommonProducer commonProducer;

    public CustomerConsumer(CustomerRepository customerRepository, CommonProducer commonProducer) {
        this.customerRepository = customerRepository;
        this.commonProducer = commonProducer;
    }

    @KafkaListener(topics = "addCustomer", groupId = "customerGroup", concurrency = "1")
    public void addCustomer(Customer customerEvent) {
        log.info("Received addCustomer event for ID: {}", customerEvent.getId());
        Customer newCustomer = customerRepository.save(customerEvent);
        commonProducer.sendKafkaEvent("newCustomer", newCustomer.toJson(), "customer");
    }

    @KafkaListener(topics = "updateCustomer", groupId = "customerGroup")
    public void updateCustomer(Customer customerEvent) {
        log.info("Received updateCustomer event for ID: {}", customerEvent.getId());
        customerRepository.findById(customerEvent.getId())
            .ifPresentOrElse(customer -> {
                customer.setName(customerEvent.getName());
                customer.setEmail(customerEvent.getEmail());
                customer.setPhone(customerEvent.getPhone());
                customerRepository.save(customer);
        }, () -> log.warn("Customer not found with ID: {}", customerEvent.getId()));
    }
}
