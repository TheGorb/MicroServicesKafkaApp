package com.balanceservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.balanceservices", "com.commonmessaging"})
public class BalanceServicesApplication {
    public static void main(String[] args) {
        SpringApplication.run(BalanceServicesApplication.class, args);
    }
}
