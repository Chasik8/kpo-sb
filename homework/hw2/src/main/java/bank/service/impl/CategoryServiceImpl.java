package bank.service.impl;

import bank.domain.Category;
import bank.domain.OperationType;
import bank.exception.BusinessLogicException;
import bank.exception.ValidationException;
import bank.repository.CategoryRepository;
import bank.service.CategoryService;

import java.util.List;

/**
 * Реализация {@link CategoryService}.
 */
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(String name, OperationType type) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Category name cannot be empty");
        }
        if (type == null) {
            throw new ValidationException("Category type must be set");
        }

        Category category = new Category(
                System.currentTimeMillis(), 
                type,
                name
        );
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException("Category not found: " + id));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void updateCategoryName(Long id, String newName) {
        Category category = getCategoryById(id);
        category.setName(newName);
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}