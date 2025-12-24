package com.gozon.orders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gozon.orders.domain.Order;
import com.gozon.orders.domain.OutboxEvent;
import com.gozon.orders.repository.OrderRepository;
import com.gozon.orders.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public Order createOrder(Long userId, BigDecimal amount, String description) {
        // 1. Создаем заказ
        Order order = Order.builder()
                .userId(userId)
                .amount(amount)
                .description(description)
                .status(Order.OrderStatus.NEW)
                .createdAt(LocalDateTime.now())
                .build();
        order = orderRepository.save(order);

        // 2. Пишем в Outbox
        try {
            String payload = objectMapper.writeValueAsString(new OrderPaymentRequest(order.getId(), userId, amount));
            OutboxEvent event = OutboxEvent.builder()
                    .aggregateId(order.getId().toString())
                    .payload(payload)
                    .createdAt(LocalDateTime.now())
                    .processed(false)
                    .build();
            outboxRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return order;
    }

    @Transactional
    public void updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(Order.OrderStatus.valueOf(status));
        orderRepository.save(order);

        // Push уведомление в WebSocket
        messagingTemplate.convertAndSend("/topic/orders/" + order.getUserId(), new StatusUpdate(orderId, status));
    }

    public Order getOrder(Long id) { return orderRepository.findById(id).orElseThrow(); }
    public List<Order> getAllOrders() { return orderRepository.findAll(); }

    public record OrderPaymentRequest(Long orderId, Long userId, BigDecimal amount) {}
    public record StatusUpdate(Long orderId, String status) {}
}