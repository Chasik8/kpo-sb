package bank.config;

import bank.command.*;
import bank.dto.CreateAccountRequest;
import bank.dto.CreateCategoryRequest;
import bank.dto.CreateOperationRequest;
import bank.exception.BusinessLogicException;
import bank.facade.FinanceFacade;
import bank.factory.DomainFactory;
import bank.io.export.JsonExportVisitor;
import bank.io.importing.AbstractDataImporter;
import bank.io.importing.JsonDataImporter;
import bank.repository.*;
import bank.repository.impl.InMemoryBankAccountRepository;
import bank.repository.impl.InMemoryCategoryRepository;
import bank.repository.impl.InMemoryOperationRepository;
import bank.repository.proxy.CachingOperationRepositoryProxy;
import bank.service.*;
import bank.service.impl.*;

import java.time.LocalDate;

/**
 * Простой DI-контейнер для управления зависимостями.
 * Инициализирует и связывает все сервисы, репозитории и фасады при старте.
 * Демонстрирует принцип инверсии зависимостей (DIP).
 */
public class ApplicationContext {


    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;
    private final OperationRepository operationRepository;


    private final AccountService accountService;
    private final CategoryService categoryService;
    private final OperationService operationService;
    private final AnalyticsService analyticsService;


    private final DomainFactory domainFactory;


    private final FinanceFacade financeFacade;

    public ApplicationContext() {

        this.bankAccountRepository = new InMemoryBankAccountRepository();
        this.categoryRepository = new InMemoryCategoryRepository();



        OperationRepository realOperationRepository = new InMemoryOperationRepository();
        this.operationRepository = new CachingOperationRepositoryProxy(realOperationRepository);


        this.domainFactory = new DomainFactory();


        this.accountService = new AccountServiceImpl(bankAccountRepository);
        this.categoryService = new CategoryServiceImpl(categoryRepository);
        this.operationService = new OperationServiceImpl(operationRepository, accountService, categoryService);
        this.analyticsService = new AnalyticsServiceImpl(operationRepository);


        this.financeFacade = new FinanceFacade(
                accountService,
                categoryService,
                operationService,
                analyticsService
        );
    }





    public Command getCreateAccountCommand(CreateAccountRequest request) {
        return new CreateAccountCommand(financeFacade, request);
    }

    public Command getCreateCategoryCommand(CreateCategoryRequest request) {



        return new CreateCategoryCommand(financeFacade, request);
    }

    public Command getAddOperationCommand(CreateOperationRequest request) {
        return new AddOperationCommand(financeFacade, domainFactory, request);
    }

    public Command getAnalyticsCommand(LocalDate from, LocalDate to) {
        return new GetAnalyticsCommand(financeFacade, from, to);
    }

    public Command getExportDataCommand(String filePath) {


        return new ExportDataCommand(
                bankAccountRepository,
                categoryRepository,
                operationRepository,
                new JsonExportVisitor(), 
                filePath
        );
    }

    public Command getImportDataCommand(String filePath) {

        AbstractDataImporter importer = new JsonDataImporter(
                bankAccountRepository,
                categoryRepository,
                operationRepository
        );
        return new ImportDataCommand(importer, filePath);
    }
}