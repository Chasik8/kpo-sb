package com.gozon.payments.service;

import com.gozon.payments.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void processOutbox() {
        var events = outboxRepository.findByProcessedFalse();
        for (var event : events) {
            kafkaTemplate.send("payment-results", event.getAggregateId(), event.getPayload());
            event.setProcessed(true);
            outboxRepository.save(event);
        }
    }
}