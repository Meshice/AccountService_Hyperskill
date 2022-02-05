package account.request;

import javax.validation.constraints.Pattern;

public class UserRoleChangeRequest {

    private String user;
    private String role;
    @Pattern(regexp = "GRANT|REMOVE", message = "Use only GRAND and REMOVE operations!")
    private String operation;

    public UserRoleChangeRequest() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
