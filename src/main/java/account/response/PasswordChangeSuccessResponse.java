package account.response;

public class PasswordChangeSuccessResponse {

    private String email;
    private String status;

    public PasswordChangeSuccessResponse(String email, String status) {
        this.email = email;
        this.status = status;
    }

    public PasswordChangeSuccessResponse() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
