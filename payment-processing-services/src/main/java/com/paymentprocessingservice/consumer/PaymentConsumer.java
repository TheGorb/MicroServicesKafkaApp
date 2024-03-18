package com.paymentprocessingservice.consumer;

import com.commonmessaging.model.Customer;
import com.commonmessaging.model.Payment;
import com.commonmessaging.repository.CustomerRepository;
import com.paymentprocessingservice.repository.PaymentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;

    public PaymentConsumer(PaymentRepository paymentRepository, CustomerRepository customerRepository) {
        this.paymentRepository = paymentRepository;
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

    @KafkaListener(topics = "newPayment", groupId = "paymentGroup")
    public void consumePaymentEvent(Payment paymentEvent) {
        System.out.println("a new payment has been created: " + paymentEvent);
    }
}
