package bank.command;

import bank.io.importing.AbstractDataImporter;

/**
 * Команда для импорта данных.
 * Демонстрирует паттерн "Шаблонный метод".
 */
public class ImportDataCommand implements Command {

    private final AbstractDataImporter importer;
    private final String filePath;

    public ImportDataCommand(AbstractDataImporter importer, String filePath) {
        this.importer = importer;
        this.filePath = filePath;
    }

    @Override
    public void execute() {
        try {

            importer.importData(filePath);
            System.out.println("Data imported successfully from " + filePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to import data: " + e.getMessage(), e);
        }
    }
}