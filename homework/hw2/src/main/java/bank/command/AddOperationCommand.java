package bank.command;

import bank.domain.Operation;
import bank.dto.CreateOperationRequest;
import bank.facade.FinanceFacade;
import bank.factory.DomainFactory;

/**
 * Команда для добавления операции.
 * Использует Фабрику для создания доменного объекта и Фасад для его сохранения.
 */
public class AddOperationCommand implements Command {

    private final FinanceFacade financeFacade;
    private final DomainFactory domainFactory;
    private final CreateOperationRequest request;

    public AddOperationCommand(FinanceFacade financeFacade, DomainFactory domainFactory, CreateOperationRequest request) {
        this.financeFacade = financeFacade;
        this.domainFactory = domainFactory;
        this.request = request;
    }

    @Override
    public void execute() {

        Operation operation = domainFactory.createOperation(request);


        Operation createdOperation = financeFacade.addOperation(operation);

        System.out.println("Operation added successfully: " + createdOperation);
    }
}