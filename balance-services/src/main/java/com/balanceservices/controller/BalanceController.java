package com.balanceservices.controller;

import com.balanceservices.service.BalanceService;
import com.commonmessaging.model.Payment;
import com.commonmessaging.repository.CustomerRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(path = "/balances", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class BalanceController {
    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService, CustomerRepository customerRepository) {
        this.balanceService = balanceService;
    }

    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void addBalance(@RequestBody Payment payment) {
        balanceService.addBalance(payment);
    }

    @PostMapping(path = "/lower", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void lowerBalance(@RequestBody Payment payment) {
        balanceService.lowerBalance(payment.getCustomerId(), Double.parseDouble(payment.getAmount()));
    }
}
