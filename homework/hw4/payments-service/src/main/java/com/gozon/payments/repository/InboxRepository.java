package com.gozon.payments.repository;
import com.gozon.payments.domain.InboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InboxRepository extends JpaRepository<InboxEvent, Long> {
    boolean existsByEventId(String eventId);
    List<InboxEvent> findByStatus(String status);
}