package bank.domain;

import bank.io.export.DataVisitor;
import bank.io.export.Visitable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Доменная модель: Операция (доход/расход).
 * Реализует Visitable для паттерна "Посетитель".
 */
public class Operation implements Visitable {
    private final Long id;
    private final OperationType type;
    private final Long bankAccountId;
    private final Long categoryId;
    private final BigDecimal amount;
    private final LocalDate date;
    private String description;

    public Operation(Long id, OperationType type, Long bankAccountId, Long categoryId, BigDecimal amount, LocalDate date, String description) {
        this.id = id;
        this.type = Objects.requireNonNull(type);
        this.bankAccountId = Objects.requireNonNull(bankAccountId);
        this.categoryId = Objects.requireNonNull(categoryId);
        this.amount = Objects.requireNonNull(amount);
        this.date = Objects.requireNonNull(date);
        this.description = description;

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Operation amount must be positive.");
        }
    }

    @Override
    public void accept(DataVisitor visitor) {
        visitor.visit(this);
    }



    public Long getId() {
        return id;
    }

    public OperationType getType() {
        return type;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }

    public Long getCategoryId() {
        return categoryId;
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

    public void setDescription(String description) {
        this.description = description;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Objects.equals(id, operation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", type=" + type +
                ", bankAccountId=" + bankAccountId +
                ", categoryId=" + categoryId +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}