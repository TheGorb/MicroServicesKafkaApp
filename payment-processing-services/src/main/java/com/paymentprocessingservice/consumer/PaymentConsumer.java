package com.paymentprocessingservice.consumer;

import com.commonmessaging.model.Customer;
import com.commonmessaging.model.Payment;
import com.commonmessaging.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentConsumer {
    private final CustomerRepository customerRepository;

    public PaymentConsumer(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @KafkaListener(topics = "newCustomer", groupId = "newPayment")
    public void consumeCustomerEvent(Customer customerEvent) {
        Customer customer = customerRepository.findById(customerEvent.getId())
                .orElse(null);

        if (customer != null) {
            Payment newPayment = new Payment("0", "USD", customer.getId());
            customer.addPayment(newPayment);
            customerRepository.save(customer);
            log.info("New customer processed: {}", customerEvent.getId());
        } else {
            log.warn("Customer not found: {}", customerEvent.getId());
        }
    }

    @KafkaListener(topics = "rejectedPayment", groupId = "paymentGroup")
    public void rejectedPayment(Payment paymentEvent) {
        Customer customer = customerRepository.findById(paymentEvent.getCustomerId())
                .orElse(null);

        if (customer != null) {
            Payment newPayment = new Payment(paymentEvent.getAmount(), paymentEvent.getCurrency(), customer.getId());
            newPayment.setAccepted(false);
            customer.addPayment(newPayment);
            customerRepository.save(customer);
            log.info("Rejected payment processed for customer: {}, Payment: {}", customer.getId(), paymentEvent);
        } else {
            log.warn("Customer not found for rejected payment: {}", paymentEvent.getId());
        }
    }

    @KafkaListener(topics = "acceptedPayment", groupId = "paymentGroup")
    public void acceptedPayment(Payment paymentEvent) {
        Customer customer = customerRepository.findById(paymentEvent.getCustomerId())
                .orElse(null);

        if (customer != null) {
            Payment newPayment = new Payment(paymentEvent.getAmount(), paymentEvent.getCurrency(), customer.getId());
            newPayment.setAccepted(true);
            customer.addPayment(newPayment);
            customerRepository.save(customer);
            log.info("Accepted payment processed for customer: {}, Payment: {}", customer.getId(), paymentEvent);
        } else {
            log.warn("Customer not found for rejected payment: {}", paymentEvent.getId());
        }
    }
}
