package bank.service;

import bank.domain.Operation;

import java.util.List;

/**
 * Интерфейс сервиса для управления {@link Operation}.
 */
public interface OperationService {
    /**
     * Добавляет операцию и атомарно обновляет баланс связанного счета.
     */
    Operation addOperation(Operation operation);
    Operation getOperationById(Long id);
    List<Operation> getAllOperations();
    void deleteOperation(Long id);
}