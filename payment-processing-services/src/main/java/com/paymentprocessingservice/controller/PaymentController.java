package com.paymentprocessingservice.controller;

import com.commonmessaging.model.Payment;
import com.paymentprocessingservice.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public Payment addPayment(@RequestBody Payment payment) {
        System.out.println("PaymentController: adding payment: " + payment);
        return paymentService.addPayment(payment);
    }

    @GetMapping("/{customerId}")
    public Payment getPaymentByCustomerName(@PathVariable String customerId) {
        System.out.println("PaymentController: getting payment by customerId: " + customerId);
        return paymentService.getPaymentByCustomerId(customerId);
    }
}
