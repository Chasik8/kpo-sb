package com.gozon.payments.service;

import com.gozon.payments.domain.*;
import com.gozon.payments.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final AccountRepository accountRepository;
    private final InboxRepository inboxRepository;

    @Transactional
    public Account createAccount(Long userId) {
        if (accountRepository.existsByUserId(userId)) throw new RuntimeException("Account Exists");
        return accountRepository.save(Account.builder().userId(userId).balance(BigDecimal.ZERO).build());
    }

    @Transactional
    public Account deposit(Long userId, BigDecimal amount) {
        Account acc = accountRepository.findByUserId(userId).orElseThrow();
        acc.setBalance(acc.getBalance().add(amount));
        return accountRepository.save(acc);
    }

    public Account getBalance(Long userId) { return accountRepository.findByUserId(userId).orElseThrow(); }

    @Transactional // запись только если не дубль
    public void saveToInbox(String orderId, String payload) {
        if (inboxRepository.existsByEventId(orderId)) return;
        inboxRepository.save(InboxEvent.builder().eventId(orderId).payload(payload).status("PENDING").build());
    }
}