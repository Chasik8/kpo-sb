package com.gozon.orders.controller;

import com.gozon.orders.domain.Order;
import com.gozon.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping
    public Order create(@RequestBody CreateOrderRequest req) {
        return service.createOrder(req.userId(), req.amount(), req.description());
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) {
        return service.getOrder(id);
    }

    @GetMapping
    public List<Order> getAll() {
        return service.getAllOrders();
    }

    public record CreateOrderRequest(Long userId, BigDecimal amount, String description) {}
}