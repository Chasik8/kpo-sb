package bank.dto;

import bank.domain.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO для создания операции.
 */
public class CreateOperationRequest {
    private final OperationType type;
    private final BigDecimal amount;
    private final LocalDate date;
    private final String description;
    private final long accountId;
    private final long categoryId;

    public CreateOperationRequest(OperationType type, BigDecimal amount, LocalDate date, String description, long accountId, long categoryId) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.accountId = accountId;
        this.categoryId = categoryId;
    }

    public OperationType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getCategoryId() {
        return categoryId;
    }
}