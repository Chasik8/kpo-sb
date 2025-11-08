package bank.repository;

import bank.domain.Category;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для {@link Category}.
 */
public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAll();
    void deleteById(Long id);
    void clear();
}