package bank.repository.impl;

import bank.domain.BankAccount;
import bank.repository.BankAccountRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Реализация репозитория {@link BankAccountRepository} в памяти.
 */
public class InMemoryBankAccountRepository implements BankAccountRepository {

    private final Map<Long, BankAccount> storage = new ConcurrentHashMap<>();

    @Override
    public BankAccount save(BankAccount account) {
        storage.put(account.getId(), account);
        return account;
    }

    @Override
    public Optional<BankAccount> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<BankAccount> findAll() {
        return storage.values().stream()
                .sorted((a1, a2) -> a1.getId().compareTo(a2.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public void clear() {
        storage.clear();
    }
}