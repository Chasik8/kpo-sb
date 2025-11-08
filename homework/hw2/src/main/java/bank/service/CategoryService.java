package bank.service;

import bank.domain.Category;
import bank.domain.OperationType;

import java.util.List;

/**
 * Интерфейс сервиса для управления {@link Category}.
 */
public interface CategoryService {
    Category createCategory(String name, OperationType type);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    void updateCategoryName(Long id, String newName);
    void deleteCategory(Long id);
}