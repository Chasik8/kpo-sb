package bank.repository.proxy;

import bank.domain.Operation;
import bank.repository.OperationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Паттерн "Прокси" (Кэширующий Прокси).
 * Оборачивает "медленный" репозиторий (в нашем случае InMemory, но мог быть и DB)
 * и кэширует часто запрашиваемые данные (поиск по ID и все операции).
 *
 * При любой операции записи (save, delete) кэш сбрасывается.
 */
public class CachingOperationRepositoryProxy implements OperationRepository {

    private final OperationRepository realRepository;


    private final Map<Long, Operation> cacheById = new ConcurrentHashMap<>();
    private List<Operation> cacheAll = null;

    public CachingOperationRepositoryProxy(OperationRepository realRepository) {
        this.realRepository = realRepository;
    }

    @Override
    public Operation save(Operation operation) {
        Operation saved = realRepository.save(operation);
        invalidateCache(); 
        return saved;
    }

    @Override
    public Optional<Operation> findById(Long id) {

        Operation cached = cacheById.get(id);
        if (cached != null) {
            return Optional.of(cached);
        }


        Optional<Operation> found = realRepository.findById(id);
        found.ifPresent(op -> cacheById.put(id, op)); 
        return found;
    }

    @Override
    public List<Operation> findAll() {

        if (cacheAll != null) {
            return cacheAll;
        }


        cacheAll = realRepository.findAll();
        return cacheAll;
    }

    @Override
    public List<Operation> findByDateBetween(LocalDate from, LocalDate to) {



        return realRepository.findByDateBetween(from, to);
    }

    @Override
    public void deleteById(Long id) {
        realRepository.deleteById(id);
        invalidateCache(); 
    }

    @Override
    public void clear() {
        realRepository.clear();
        invalidateCache();
    }

    /**
     * Сбрасывает весь кэш.
     */
    private void invalidateCache() {
        cacheById.clear();
        cacheAll = null;
    }
}