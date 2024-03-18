package com.customermanagementservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.customermanagementservices", "com.commonmessaging"})
public class CustomerManagementServicesApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerManagementServicesApplication.class, args);
    }

}
