package account.response;

public class UserDeleteSuccessResponse {

    private String user;
    private String status;

    public UserDeleteSuccessResponse(String email, String status) {
        this.user = email;
        this.status = status;
    }

    public UserDeleteSuccessResponse() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
