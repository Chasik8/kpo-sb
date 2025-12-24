package com.gozon.payments.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gozon.payments.domain.*;
import com.gozon.payments.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class InboxProcessor {
    private final InboxRepository inboxRepository;
    private final AccountRepository accountRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void processPendingInboxEvents() {
        var events = inboxRepository.findByStatus("PENDING");
        for (InboxEvent event : events) {
            try {
                processPayment(event);
                event.setStatus("PROCESSED");
            } catch (Exception e) {
                event.setStatus("FAILED");
                e.printStackTrace();
            }
            inboxRepository.save(event);
        }
    }

    private void processPayment(InboxEvent event) throws Exception {
        var req = objectMapper.readValue(event.getPayload(), Req.class);
        var acc = accountRepository.findByUserId(req.userId()).orElse(null);
        String status = "CANCELLED";

        if (acc != null && acc.getBalance().compareTo(req.amount()) >= 0) {
            acc.setBalance(acc.getBalance().subtract(req.amount()));
            accountRepository.save(acc);
            status = "FINISHED";
        }

        // (Result)
        String outPayload = objectMapper.writeValueAsString(new Res(req.orderId(), status));
        outboxRepository.save(OutboxEvent.builder()
                .aggregateId(req.orderId().toString())
                .payload(outPayload)
                .processed(false).build());
    }

    record Req(Long orderId, Long userId, BigDecimal amount) {}
    record Res(Long orderId, String status) {}
}