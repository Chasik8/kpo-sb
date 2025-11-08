package bank.dto;

/**
 * DTO для создания счета.
 */
public class CreateAccountRequest {
    private final String name;

    public CreateAccountRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}