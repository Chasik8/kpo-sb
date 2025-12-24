package com.gozon.payments.controller;

import com.gozon.payments.domain.Account;
import com.gozon.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final PaymentService service;

    @PostMapping
    public Account create(@RequestBody CreateRequest r) { return service.createAccount(r.userId()); }

    @PostMapping("/{userId}/deposit")
    public Account deposit(@PathVariable Long userId, @RequestBody DepositRequest r) { return service.deposit(userId, r.amount()); }

    @GetMapping("/{userId}")
    public Account get(@PathVariable Long userId) { return service.getBalance(userId); }

    public record CreateRequest(Long userId) {}
    public record DepositRequest(BigDecimal amount) {}
}