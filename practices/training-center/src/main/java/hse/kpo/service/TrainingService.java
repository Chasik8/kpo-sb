package hse.kpo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hse.kpo.controllers.CustomerResponse;
import hse.kpo.domains.Customer;
import hse.kpo.kafka.TrainingCompletedEvent;
import hse.kpo.kafka.outbox.OutboxEvent;
import hse.kpo.kafka.outbox.OutboxEventRepository;
import hse.kpo.repositories.CustomerRepository;
import hse.kpo.utils.CustomerResponseUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TrainingService {

    private final KafkaTemplate<String, TrainingCompletedEvent> kafkaTemplate;
    private final CustomerRepository repository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public String trainCustomer(int customerId, String trainType) {
        var customer = repository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        switch (trainType) {
            case "handPower" -> customer.setHandPower(customer.getHandPower() + 1);
            case "legPower" -> customer.setLegPower(customer.getLegPower() + 1);
            case "iq" -> customer.setIq(customer.getIq() + 1);
            default -> throw new RuntimeException("bad trainType: " + trainType);
        }

        saveToOutbox(customerId, trainType);

        return "Тренировка завершена! Параметры обновлены";
    }

    @Cacheable(value = "customers")
    public List<CustomerResponse> getAllCustomers() {
        return repository.findAll().stream()
                .map(CustomerResponseUtils::convertToResponse)
                .collect(Collectors.toList());
    }

    @CacheEvict("customers")
    public void saveCustomer(Customer customer) {
        repository.save(customer);
    }

    //В идеале сделать отдельный сервис
    private void saveToOutbox(int customerId, String trainType) {
        TrainingCompletedEvent event = new TrainingCompletedEvent(
                customerId, trainType
        );

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setAggregateType("TrainingUpdates");
        outboxEvent.setEventType("TrainingCompleted");
        outboxEvent.setCreatedAt(LocalDateTime.now());

        try {
            outboxEvent.setPayload(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize customer event");
        }

        outboxEventRepository.save(outboxEvent);
    }

}
