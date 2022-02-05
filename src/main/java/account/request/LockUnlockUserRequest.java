package account.request;

import javax.validation.constraints.Pattern;

public class LockUnlockUserRequest {

    private String user;
    @Pattern(regexp = "LOCK|UNLOCK", message = "Only LOCK or UNLOCK")
    private String operation;

    public LockUnlockUserRequest() {}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
