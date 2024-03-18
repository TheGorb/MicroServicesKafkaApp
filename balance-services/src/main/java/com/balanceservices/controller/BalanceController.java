package com.balanceservices.controller;

import com.balanceservices.service.BalanceService;
import com.commonmessaging.model.Payment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balances")
public class BalanceController {
    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @PostMapping("/add")
    public void addBalance(@RequestBody Payment payment) {
        balanceService.addBalance(payment.getCustomerId(), Double.parseDouble(payment.getAmount()));
    }

    @PostMapping("/lower")
    public void lowerBalance(@RequestBody Payment payment) {
        balanceService.lowerBalance(payment.getCustomerId(), Double.parseDouble(payment.getAmount()));
    }
}
