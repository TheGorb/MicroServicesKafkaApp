package com.balanceservices.consumer;

import com.balanceservices.repository.BalanceRepository;
import com.commonmessaging.model.Balance;
import com.commonmessaging.model.Customer;
import com.commonmessaging.model.Payment;
import com.commonmessaging.producer.CommonProducer;
import com.commonmessaging.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceConsumer {
    private final BalanceRepository balanceRepository;
    private final CustomerRepository customerRepository;
    private final CommonProducer commonProducer;

    public BalanceConsumer(BalanceRepository balanceRepository, CustomerRepository customerRepository, CommonProducer commonProducer) {
        this.balanceRepository = balanceRepository;
        this.customerRepository = customerRepository;
        this.commonProducer = commonProducer;
    }

    @KafkaListener(topics = "addBalance", groupId = "balanceGroup")
    public void consumeAddBalanceEvent(Payment payment) {
        Customer customer = customerRepository.findById(payment.getCustomerId())
                .orElse(null);
        if (customer != null) {
            customer.getCustomerBalance().addBalance(payment.getAmount());
            customerRepository.save(customer);
            log.info("Balance updated for customer: {}", payment.getCustomerId());
        } else {
            log.warn("Customer not found: {}", payment.getCustomerId());
        }
    }

    @KafkaListener(topics = "lowerBalance", groupId = "balanceGroup")
    public void consumeLowerBalanceEvent(Payment paymentEvent) {
        Customer customer = customerRepository.findById(paymentEvent.getCustomerId())
                .orElse(null);

        if (customer != null && customer.getCustomerBalance() == null) {
            Balance balance = new Balance(paymentEvent.getCustomerId(), "0", "0");
            customer.setCustomerBalance(balance);
            customerRepository.save(customer);
            commonProducer.sendKafkaEvent("rejectedPayment", new Payment(paymentEvent.getAmount(), paymentEvent.getCurrency(), paymentEvent.getCustomerId()).toJson(), "payment");
            log.info("Payment rejected due to no balance for customer: {}", paymentEvent.getCustomerId());
        } else if (customer == null) {
            log.warn("Customer not found: {}", paymentEvent.getCustomerId());
        } else {
            if (Double.parseDouble(paymentEvent.getAmount()) > Double.parseDouble(customer.getCustomerBalance().getBalance())) {
                commonProducer.sendKafkaEvent("rejectedPayment", new Payment(paymentEvent.getAmount(), paymentEvent.getCurrency(), paymentEvent.getCustomerId()).toJson(), "payment");
                log.warn("Payment rejected for customer: {}, insufficient funds.", paymentEvent.getCustomerId());
            } else {
                customer.getCustomerBalance().lowerBalance(String.valueOf(paymentEvent.getAmount()));
                balanceRepository.save(customer.getCustomerBalance());
                commonProducer.sendKafkaEvent("acceptedPayment", new Payment(paymentEvent.getAmount(), paymentEvent.getCurrency(), paymentEvent.getCustomerId()).toJson(), "payment");
                log.info("Payment accepted and balance lowered for customer: {}", paymentEvent.getCustomerId());
            }
        }
    }

    @KafkaListener(topics = "newCustomer", groupId = "newCustomerBalance")
    public void consumeNewCustomerBalance(Customer customerEvent) {
        Customer customer = customerRepository.findById(customerEvent.getId())
                .orElse(null);

        if (customer != null) {
            customer.setCustomerBalance(new Balance(customer.getId(), "0", "0"));
            customerRepository.save(customer);
            log.info("New customer balance initialized for customer: {}", customerEvent.getId());
        } else {
            log.warn("New customer not found for initialization: {}", customerEvent.getId());
        }
    }
}
