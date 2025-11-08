package bank.domain;

import bank.io.export.DataVisitor;
import bank.io.export.Visitable;

import java.util.Objects;

/**
 * Доменная модель: Категория (доход/расход).
 * Реализует Visitable для паттерна "Посетитель".
 */
public class Category implements Visitable {
    private final Long id;
    private final OperationType type;
    private String name;

    public Category(Long id, OperationType type, String name) {
        this.id = id;
        this.type = Objects.requireNonNull(type);
        this.name = Objects.requireNonNull(name);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}