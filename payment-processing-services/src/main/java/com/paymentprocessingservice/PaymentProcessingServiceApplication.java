package com.paymentprocessingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.paymentprocessingservice", "com.commonmessaging"})
public class PaymentProcessingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentProcessingServiceApplication.class, args);
    }
}
