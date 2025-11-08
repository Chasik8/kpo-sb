package bank.io.export;

/**
 * Паттерн "Посетитель": Интерфейс для "посещаемых" объектов (доменных).
 */
public interface Visitable {
    /**
     * "Принимает" посетителя.
     */
    void accept(DataVisitor visitor);
}