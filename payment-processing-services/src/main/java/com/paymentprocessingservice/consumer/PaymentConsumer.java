package com.paymentprocessingservice.consumer;

import com.commonmessaging.model.Customer;
import com.commonmessaging.model.Payment;
import com.commonmessaging.repository.CustomerRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {
    private final CustomerRepository customerRepository;

    public PaymentConsumer(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @KafkaListener(topics = "newCustomer", groupId = "newPayment", concurrency = "2")
    public void consumeCustomerEvent(Customer customerEvent) {
        Customer customer = customerRepository.findById(customerEvent.getId())
                .orElse(null);

        if (customer != null) {
            Payment newPayment = new Payment("0", "USD", customer.getId());
            customer.addPayment(newPayment);
            customerRepository.save(customer);
        } else {
            System.out.println("customer not found: " + customerEvent.getId());
        }
    }

    @KafkaListener(topics = "rejectedPayment", groupId = "paymentGroup")
    public void rejectedPayment(Payment paymentEvent) {
        Customer customer = customerRepository.findById(paymentEvent.getId())
                .orElse(null);

        if (customer != null) {
            Payment newPayment = new Payment(paymentEvent.getAmount(), paymentEvent.getCurrency(), customer.getId());
            newPayment.setAccepted(false);
            customer.addPayment(newPayment);
            customerRepository.save(customer);
            System.out.println("a new payment has been created: " + paymentEvent);
        } else {
            System.out.println("customer not found: " + paymentEvent.getId());
        }
    }

    @KafkaListener(topics = "acceptedPayment", groupId = "paymentGroup")
    public void acceptedPayment(Payment paymentEvent) {
        Customer customer = customerRepository.findById(paymentEvent.getId())
                .orElse(null);

        if (customer != null) {
            Payment newPayment = new Payment(paymentEvent.getAmount(), paymentEvent.getCurrency(), customer.getId());
            newPayment.setAccepted(true);
            customer.addPayment(newPayment);
            customerRepository.save(customer);
            System.out.println("a new payment has been created: " + paymentEvent);
        } else {
            System.out.println("customer not found: " + paymentEvent.getId());
        }
    }
}
