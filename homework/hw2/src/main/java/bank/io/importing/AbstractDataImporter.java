package bank.io.importing;

import bank.domain.BankAccount;
import bank.domain.Category;
import bank.domain.Operation;
import bank.repository.BankAccountRepository;
import bank.repository.CategoryRepository;
import bank.repository.OperationRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Паттерн "Шаблонный метод".
 * Определяет скелет алгоритма импорта данных.
 */
public abstract class AbstractDataImporter {


    protected final BankAccountRepository bankAccountRepository;
    protected final CategoryRepository categoryRepository;
    protected final OperationRepository operationRepository;

    public AbstractDataImporter(BankAccountRepository bankAccountRepository,
                                CategoryRepository categoryRepository,
                                OperationRepository operationRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
    }

    /**
     * Шаблонный метод.
     * Определяет шаги алгоритма импорта.
     */
    public final void importData(String filePath) throws IOException {

        String content = readFileContent(filePath);


        ParsedData data = parseContent(content);


        saveData(data);
    }

    /**
     * Общий шаг: чтение файла в строку.
     */
    protected String readFileContent(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    /**
     * Общий шаг: сохранение данных в репозитории.
     * (Очищает старые данные перед импортом)
     */
    protected void saveData(ParsedData data) {

        bankAccountRepository.clear();
        categoryRepository.clear();
        operationRepository.clear();

        data.accounts.forEach(bankAccountRepository::save);
        data.categories.forEach(categoryRepository::save);
        data.operations.forEach(operationRepository::save);
    }

    /**
     * Абстрактный шаг: парсинг контента.
     * Должен быть реализован подклассами (JsonDataImporter, CsvDataImporter...).
     */
    protected abstract ParsedData parseContent(String content);

    /**
     * Внутренний класс-контейнер для передачи данных
     * между шагами парсинга и сохранения.
     */
    protected static class ParsedData {
        final List<BankAccount> accounts;
        final List<Category> categories;
        final List<Operation> operations;

        public ParsedData(List<BankAccount> accounts, List<Category> categories, List<Operation> operations) {
            this.accounts = accounts;
            this.categories = categories;
            this.operations = operations;
        }
    }
}