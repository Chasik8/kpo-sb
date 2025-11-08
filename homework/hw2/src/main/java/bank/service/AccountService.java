package bank.service;

import bank.domain.BankAccount;
import bank.domain.Operation;

import java.util.List;

/**
 * Интерфейс сервиса для управления {@link BankAccount}.
 * Определяет бизнес-логику.
 */
public interface AccountService {
    BankAccount createAccount(String name);
    BankAccount getAccountById(Long id);
    List<BankAccount> getAllAccounts();
    void updateAccountName(Long id, String newName);
    void deleteAccount(Long id);

    /**
     * Внутренний метод, вызываемый {@link OperationService}
     * для атомарного изменения баланса при добавлении операции.
     */
    void applyOperation(Operation operation);
}