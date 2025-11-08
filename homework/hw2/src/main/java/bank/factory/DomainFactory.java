package bank.factory;

import bank.domain.BankAccount;
import bank.domain.Category;
import bank.domain.Operation;
import bank.domain.OperationType;
import bank.dto.CreateOperationRequest;
import bank.exception.ValidationException;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Паттерн "Фабрика".
 * Централизует создание доменных объектов, обеспечивая их валидность
 * и инкапсулируя логику генерации ID.
 */
public class DomainFactory {


    private final AtomicLong accountIdGenerator = new AtomicLong(0);
    private final AtomicLong categoryIdGenerator = new AtomicLong(0);
    private final AtomicLong operationIdGenerator = new AtomicLong(0);

    /**
     * Создает новый {@link BankAccount}.
     *
     * @param name Название счета
     * @return Созданный BankAccount
     */
    public BankAccount createBankAccount(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Account name cannot be empty.");
        }
        long id = accountIdGenerator.incrementAndGet();

        return new BankAccount(id, name, BigDecimal.ZERO);
    }

    /**
     * Создает новую {@link Category}.
     *
     * @param name Название категории
     * @param type Тип (Доход/Расход)
     * @return Созданная Category
     */
    public Category createCategory(String name, OperationType type) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Category name cannot be empty.");
        }
        if (type == null) {
            throw new ValidationException("Category type must be set.");
        }
        long id = categoryIdGenerator.incrementAndGet();
        return new Category(id, type, name);
    }

    /**
     * Создает новую {@link Operation} из DTO.
     *
     * @param request DTO с данными для создания
     * @return Созданная Operation
     */
    public Operation createOperation(CreateOperationRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Operation amount must be positive.");
        }
        if (request.getType() == null) {
            throw new ValidationException("Operation type must be set.");
        }
        if (request.getDate() == null) {
            throw new ValidationException("Operation date must be set.");
        }

        long id = operationIdGenerator.incrementAndGet();
        return new Operation(
                id,
                request.getType(),
                request.getAccountId(),
                request.getCategoryId(),
                request.getAmount(),
                request.getDate(),
                request.getDescription()
        );
    }
}