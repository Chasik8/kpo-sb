package bank.service.impl;

import bank.domain.BankAccount;
import bank.domain.Operation;
import bank.domain.OperationType;
import bank.exception.BusinessLogicException;
import bank.exception.ValidationException;
import bank.repository.BankAccountRepository;
import bank.service.AccountService;

import java.math.BigDecimal;
import java.util.List;

/**
 * Реализация {@link AccountService}.
 * Демонстрирует DIP - зависит от абстракции {@link BankAccountRepository}.
 */
public class AccountServiceImpl implements AccountService {

    private final BankAccountRepository accountRepository;

    public AccountServiceImpl(BankAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public BankAccount createAccount(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Account name cannot be empty");
        }












































































        BankAccount account = new BankAccount(
                System.currentTimeMillis(), 
                name,
                BigDecimal.ZERO
        );














        return accountRepository.save(account);
    }

    @Override
    public BankAccount getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException("Account not found: " + id));
    }

    @Override
    public List<BankAccount> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public void updateAccountName(Long id, String newName) {
        BankAccount account = getAccountById(id);
        account.setName(newName);
        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long id) {


        accountRepository.deleteById(id);
    }

    @Override
    public void applyOperation(Operation operation) {
        BankAccount account = getAccountById(operation.getBankAccountId());

        if (operation.getType() == OperationType.INCOME) {
            account.deposit(operation.getAmount());
        } else {
            account.withdraw(operation.getAmount());
        }

        accountRepository.save(account);
    }
}