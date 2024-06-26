package com.paymentprocessingservice.service;

import com.commonmessaging.model.Payment;
import com.paymentprocessingservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import com.commonmessaging.producer.CommonProducer;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    private final CommonProducer commonProducer;

    public PaymentService(PaymentRepository paymentRepository, CommonProducer commonProducer) {
        this.paymentRepository = paymentRepository;
        this.commonProducer = commonProducer;
    }

    public Payment addPayment(Payment payment) {
        Payment savedPayment = paymentRepository.save(payment);
        commonProducer.sendKafkaEvent("newPayment", savedPayment.toJson(), "payment");
        return savedPayment;
    }

    public Payment getPaymentByCustomerId(String customerId) {
        return paymentRepository.findByCustomerId(customerId);
    }
}
