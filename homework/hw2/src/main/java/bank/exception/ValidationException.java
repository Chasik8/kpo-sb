package bank.exception;

/**
 * Исключение для ошибок валидации данных.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}