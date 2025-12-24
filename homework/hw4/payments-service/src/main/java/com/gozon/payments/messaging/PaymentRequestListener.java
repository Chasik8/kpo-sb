package com.gozon.payments.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gozon.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentRequestListener {
    private final PaymentService service;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "payment-requests", groupId = "payments-group")
    public void listen(String message) {
        try {
            var req = mapper.readValue(message, Req.class);
            service.saveToInbox(req.orderId().toString(), message);
        } catch (Exception e) { e.printStackTrace(); }
    }
    record Req(Long orderId) {}
}