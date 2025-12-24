package com.gozon.orders.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gozon.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentResultListener {
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payment-results", groupId = "orders-group")
    public void listen(String message) {
        try {
            PaymentResult res = objectMapper.readValue(message, PaymentResult.class);
            orderService.updateStatus(res.orderId(), res.status());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public record PaymentResult(Long orderId, String status) {}
}