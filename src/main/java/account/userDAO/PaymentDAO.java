package account.userDAO;

import account.entity.Payment;
import account.request.UpdatePaymentRequest;
import account.response.PaymentUserInfo;

import java.util.List;

public interface PaymentDAO {


    void addPayment(List<Payment> payment);

    boolean findEmailPeriodUnique(Payment payment);

    void updatePaymentByEmployeePeriod(UpdatePaymentRequest payment);

    List<PaymentUserInfo> getInfoUserByPeriod(String period, String employee);
}
