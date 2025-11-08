package bank.repository;

import bank.domain.BankAccount;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для {@link BankAccount}.
 * Абстрагирует слой доступа к данным (принцип DIP).
 */
public interface BankAccountRepository {
    BankAccount save(BankAccount account);
    Optional<BankAccount> findById(Long id);
    List<BankAccount> findAll();
    void deleteById(Long id);
    void clear();
}