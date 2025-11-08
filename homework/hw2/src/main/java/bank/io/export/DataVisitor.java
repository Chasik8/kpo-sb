package bank.io.export;

import bank.domain.BankAccount;
import bank.domain.Category;
import bank.domain.Operation;

/**
 * Паттерн "Посетитель": Интерфейс "посетителя".
 * Определяет методы visit() для каждого типа доменного объекта.
 */
public interface DataVisitor {
    void visit(BankAccount account);
    void visit(Category category);
    void visit(Operation operation);

    /**
     * @return Результат обхода (например, строка JSON).
     */
    String getResult();
}