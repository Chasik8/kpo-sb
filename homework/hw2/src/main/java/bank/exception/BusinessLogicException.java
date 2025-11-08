package bank.exception;

/**
 * Исключение для ошибок бизнес-логики (напр., не найдена сущность).
 */
public class BusinessLogicException extends RuntimeException {
    public BusinessLogicException(String message) {
        super(message);
    }
}