package com.customermanagementservices.consumer;

import com.commonmessaging.model.Customer;
import com.customermanagementservices.repository.CustomerRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CustomerConsumer {
    private final CustomerRepository customerRepository;

    public CustomerConsumer(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @KafkaListener(topics = "addCustomer", groupId = "customerGroup")
    public void addCustomer(Customer customerEvent) {
        System.out.println("Consumed the add customer event from Kafka: " + customerEvent.toString());
        customerRepository.save(customerEvent);
    }

    @KafkaListener(topics = "updateCustomer", groupId = "customerGroup")
    public void updateCustomer(Customer customerEvent) {
        System.out.println(customerEvent.toString());
        customerRepository.findById(customerEvent.getId())
                .ifPresent(customer -> {
                    customer.setName(customerEvent.getName());
                    customer.setEmail(customerEvent.getEmail());
                    customer.setPhone(customerEvent.getPhone());
                    customerRepository.save(customer);
        });
        System.out.println("Consumed the update customer event from Kafka: " + customerEvent);
    }
}
