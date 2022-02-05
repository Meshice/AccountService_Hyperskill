package account.response;

public class PaymentAddSuccessResponse {

    private String status;

    public PaymentAddSuccessResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
