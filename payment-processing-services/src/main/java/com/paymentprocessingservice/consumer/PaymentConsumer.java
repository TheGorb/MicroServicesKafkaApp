package com.paymentprocessingservice.consumer;

import com.commonmessaging.model.Customer;
import com.commonmessaging.model.Payment;
import com.paymentprocessingservice.repository.PaymentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {
    private final PaymentRepository paymentRepository;

    public PaymentConsumer(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @KafkaListener(topics = "newCustomer", groupId = "customerGroup", concurrency = "1")
    public void consumeCustomerEvent(Customer customerEvent) {
        paymentRepository.save(new Payment("0", "0", customerEvent.getId()));
    }

    @KafkaListener(topics = "newPayment", groupId = "paymentGroup", concurrency = "1")
    public void consumePaymentEvent(Payment paymentEvent) {
        System.out.println("a new customer has been created: " + paymentEvent);
    }
}
