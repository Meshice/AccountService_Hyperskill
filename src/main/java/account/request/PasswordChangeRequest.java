package account.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PasswordChangeRequest {

    @Size(min = 12, message = "PasswordError")
    @NotBlank
    private String new_password;

    public PasswordChangeRequest() {
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    @Override
    public String toString() {
        return "PasswordChangeRequest{" +
                "new_password='" + new_password + '\'' +
                '}';
    }
}
