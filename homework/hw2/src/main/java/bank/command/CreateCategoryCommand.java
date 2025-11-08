package bank.command;

import bank.domain.Category;
import bank.dto.CreateCategoryRequest;
import bank.facade.FinanceFacade;

/**
 * Команда для создания категории.
 */
public class CreateCategoryCommand implements Command {

    private final FinanceFacade financeFacade;
    private final CreateCategoryRequest request;

    public CreateCategoryCommand(FinanceFacade financeFacade, CreateCategoryRequest request) {
        this.financeFacade = financeFacade;
        this.request = request;
    }

    @Override
    public void execute() {
        Category category = financeFacade.createCategory(request);
        System.out.println("Category created successfully: " + category);
    }
}