package com.gozon.payments.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inbox")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String eventId; // ID Заказа

    @Column(columnDefinition = "TEXT")
    private String payload;
    private String status; // PENDING, PROCESSED
}