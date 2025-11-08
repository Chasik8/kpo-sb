package bank.facade;

import bank.domain.BankAccount;
import bank.domain.Category;
import bank.domain.Operation;
import bank.dto.AnalyticsReport;
import bank.dto.CreateAccountRequest;
import bank.dto.CreateCategoryRequest;
import bank.service.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Паттерн "Фасад".
 * Предоставляет упрощенный и единый интерфейс к подсистеме сервисов.
 * Используется командами и консольным UI для выполнения бизнес-операций.
 */
public class FinanceFacade {
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final OperationService operationService;
    private final AnalyticsService analyticsService;


    public FinanceFacade(AccountService accountService,
                         CategoryService categoryService,
                         OperationService operationService,
                         AnalyticsService analyticsService) {
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.operationService = operationService;
        this.analyticsService = analyticsService;
    }



    public BankAccount createAccount(CreateAccountRequest request) {
        return accountService.createAccount(request.getName());
    }

    public Category createCategory(CreateCategoryRequest request) {
        return categoryService.createCategory(request.getName(), request.getType());
    }

    public Operation addOperation(Operation operation) {
        return operationService.addOperation(operation);
    }

    public List<BankAccount> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    public List<Operation> getAllOperations() {
        return operationService.getAllOperations();
    }

    public AnalyticsReport getAnalytics(LocalDate from, LocalDate to) {
        return analyticsService.calculateAnalytics(from, to);
    }
}