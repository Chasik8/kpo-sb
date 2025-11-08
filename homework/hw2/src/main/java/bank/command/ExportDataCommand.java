package bank.command;

import bank.domain.BankAccount;
import bank.domain.Category;
import bank.domain.Operation;
import bank.io.export.DataVisitor;
import bank.repository.BankAccountRepository;
import bank.repository.CategoryRepository;
import bank.repository.OperationRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Команда для экспорта данных.
 * Демонстрирует паттерн "Посетитель".
 */
public class ExportDataCommand implements Command {

    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;
    private final OperationRepository operationRepository;
    private final DataVisitor visitor; 
    private final String filePath;

    public ExportDataCommand(BankAccountRepository bankAccountRepository, CategoryRepository categoryRepository, OperationRepository operationRepository, DataVisitor visitor, String filePath) {
        this.bankAccountRepository = bankAccountRepository;
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
        this.visitor = visitor;
        this.filePath = filePath;
    }

    @Override
    public void execute() {

        List<BankAccount> accounts = bankAccountRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        List<Operation> operations = operationRepository.findAll();



        accounts.forEach(account -> account.accept(visitor));
        categories.forEach(category -> category.accept(visitor));
        operations.forEach(operation -> operation.accept(visitor));


        String result = visitor.getResult();


        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(result);
            System.out.println("Data exported successfully to " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data: " + e.getMessage(), e);
        }
    }
}