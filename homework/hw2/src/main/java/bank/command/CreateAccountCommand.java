package bank.command;

import bank.domain.BankAccount;
import bank.dto.CreateAccountRequest;
import bank.facade.FinanceFacade;

/**
 * Команда для создания счета.
 */
public class CreateAccountCommand implements Command {

    private final FinanceFacade financeFacade;
    private final CreateAccountRequest request;

    public CreateAccountCommand(FinanceFacade financeFacade, CreateAccountRequest request) {
        this.financeFacade = financeFacade;
        this.request = request;
    }

    @Override
    public void execute() {
        BankAccount account = financeFacade.createAccount(request);
        System.out.println("Account created successfully: " + account);
    }
}