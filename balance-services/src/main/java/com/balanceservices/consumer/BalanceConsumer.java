package com.balanceservices.consumer;

import com.balanceservices.repository.BalanceRepository;
import com.commonmessaging.model.Balance;
import com.commonmessaging.model.Customer;
import com.commonmessaging.model.Payment;
import com.commonmessaging.repository.CustomerRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BalanceConsumer {
    private final BalanceRepository balanceRepository;
    private final CustomerRepository customerRepository;

    public BalanceConsumer(BalanceRepository balanceRepository, CustomerRepository customerRepository) {
        this.balanceRepository = balanceRepository;
        this.customerRepository = customerRepository;
    }

    @KafkaListener(topics = "addBalance", groupId = "balanceGroup")
    public void consumeAddBalanceEvent(Payment paymentEvent) {
        System.out.println("add balance : " + paymentEvent);

/*        Balance balance = balanceRepository.findByCustomerId(customerId);
        if (balance == null) {
            balance = new Balance(customerId, "0", "0");
        } else {
            balance.addBalance(String.valueOf(amount));
        }
        balanceRepository.save(balance);*/
    }

    @KafkaListener(topics = "lowBalance", groupId = "balanceGroup")
    public void consumeLowerBalanceEvent(Payment paymentEvent) {
        /*Balance balance = balanceRepository.findByCustomerId(customerId);
        if (balance == null) {
            balance = new Balance(customerId, "0", "0");
        } else {
            balance.lowerBalance(String.valueOf(amount));
        }
        balanceRepository.save(balance);*/
        System.out.println("lower balance : " + paymentEvent);
    }

    @KafkaListener(topics = "newCustomer", groupId = "newCustomerBalance", concurrency = "2")
    public void consumeNewCustomerBalance(Customer customerEvent) {
        Customer customer = customerRepository.findById(customerEvent.getId())
                .orElse(null);

        if (customer != null) {
            customer.setCustomerBalance(new Balance(customer.getId(), "0", "0"));
            customerRepository.save(customer);
        } else {
            System.out.println("customer not found: " + customerEvent.getId());
        }
    }
}
