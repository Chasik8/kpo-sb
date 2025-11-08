package bank.domain;

import bank.io.export.DataVisitor;
import bank.io.export.Visitable;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Доменная модель: Банковский счет.
 * Реализует Visitable для паттерна "Посетитель".
 */
public class BankAccount implements Visitable {
    private final Long id;
    private String name;
    private BigDecimal balance;

    public BankAccount(Long id, String name, BigDecimal balance) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.balance = Objects.requireNonNull(balance);
    }

    @Override
    public void accept(DataVisitor visitor) {
        visitor.visit(this);
    }



    /**
     * Увеличивает баланс счета (Доход).
     */
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }

    /**
     * Уменьшает баланс счета (Расход).
     */
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        this.balance = this.balance.subtract(amount);
    }



    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }
}