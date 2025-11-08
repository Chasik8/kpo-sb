package bank.repository;

import bank.domain.Operation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для {@link Operation}.
 */
public interface OperationRepository {
    Operation save(Operation operation);
    Optional<Operation> findById(Long id);
    List<Operation> findAll();
    List<Operation> findByDateBetween(LocalDate from, LocalDate to);
    void deleteById(Long id);
    void clear();
}