package bank.dto;

import bank.domain.OperationType;

/**
 * DTO для создания категории.
 */
public class CreateCategoryRequest {
    private final String name;
    private final OperationType type;

    public CreateCategoryRequest(String name, OperationType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public OperationType getType() {
        return type;
    }
}