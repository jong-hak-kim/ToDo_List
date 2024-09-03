package todo.common.enums;

public enum Role {
    ADMIN("admin"),
    USER("user");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
