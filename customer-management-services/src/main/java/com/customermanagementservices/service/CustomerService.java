package com.customermanagementservices.service;

import com.customermanagementservices.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import com.commonmessaging.model.Customer;
import com.commonmessaging.producer.CommonProducer;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    private final CommonProducer commonProducer;

    public CustomerService(CustomerRepository customerRepository, CommonProducer commonProducer) {
        this.customerRepository = customerRepository;
        this.commonProducer = commonProducer;
    }

    public Customer addCustomer(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        commonProducer.sendKafkaEvent("addCustomer", savedCustomer.toJson());
        return savedCustomer;
    }

    public Customer updateCustomer(Customer customer) {
        Customer updatedCustomer = customerRepository.save(customer);
        commonProducer.sendKafkaEvent("updateCustomer", updatedCustomer);
        return updatedCustomer;
    }

    public Customer getCustomerById(String id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customer getCustomerByName(String name) {
        return customerRepository.findByName(name);
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer getCustomerByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }

    public void deleteCustomerById(String id) {
        customerRepository.deleteById(id);
    }
}
