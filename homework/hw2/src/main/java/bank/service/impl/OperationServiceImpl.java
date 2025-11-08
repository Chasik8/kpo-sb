package bank.service.impl;

import bank.domain.Category;
import bank.domain.Operation;
import bank.exception.BusinessLogicException;
import bank.exception.ValidationException;
import bank.repository.OperationRepository;
import bank.service.AccountService;
import bank.service.CategoryService;
import bank.service.OperationService;

import java.util.List;

/**
 * Реализация {@link OperationService}.
 * Координирует работу с {@link AccountService} для обновления баланса.
 */
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;
    private final AccountService accountService;
    private final CategoryService categoryService;

    public OperationServiceImpl(OperationRepository operationRepository,
                                AccountService accountService,
                                CategoryService categoryService) {
        this.operationRepository = operationRepository;
        this.accountService = accountService;
        this.categoryService = categoryService;
    }

    @Override
    public Operation addOperation(Operation operation) {


        accountService.getAccountById(operation.getBankAccountId());


        Category category = categoryService.getCategoryById(operation.getCategoryId());



        if (operation.getType() != category.getType()) {
            throw new ValidationException("Operation type (" + operation.getType() +
                    ") does not match category type (" + category.getType() + ")");
        }



        try {
            accountService.applyOperation(operation);
        } catch (Exception e) {

            throw new BusinessLogicException("Failed to apply operation to account: " + e.getMessage());
        }


        return operationRepository.save(operation);
    }

    @Override
    public Operation getOperationById(Long id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException("Operation not found: " + id));
    }

    @Override
    public List<Operation> getAllOperations() {

        return operationRepository.findAll();
    }

    @Override
    public void deleteOperation(Long id) {


        operationRepository.deleteById(id);
    }
}