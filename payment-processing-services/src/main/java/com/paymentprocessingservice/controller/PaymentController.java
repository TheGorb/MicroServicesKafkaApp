package com.paymentprocessingservice.controller;

import com.commonmessaging.model.Payment;
import com.paymentprocessingservice.service.PaymentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/payments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Payment addPayment(@RequestBody Payment payment) {
        System.out.println("PaymentController: adding payment: " + payment);
        return paymentService.addPayment(payment);
    }

    @GetMapping(path = "/{customerId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Payment getPaymentByCustomerName(@PathVariable String customerId) {
        System.out.println("PaymentController: getting payment by customerId: " + customerId);
        return paymentService.getPaymentByCustomerId(customerId);
    }
}
