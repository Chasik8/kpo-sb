package bank.command;

/**
 * Паттерн "Команда".
 * Интерфейс для всех пользовательских сценариев.
 */
public interface Command {
    /**
     * Выполняет инкапсулированный сценарий.
     */
    void execute();
}