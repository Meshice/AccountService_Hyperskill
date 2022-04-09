package account.service;

import account.entity.Payment;
import account.request.UpdatePaymentRequest;
import account.response.PaymentUserInfo;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface PaymentService {

    void addPayment(List<Payment> request);

    boolean checkUniquePayment(Payment payment);

    void updatePaymentByEmployeePeriod(UpdatePaymentRequest payment);

    List<PaymentUserInfo> getInfoUserByPeriod(String period, UserDetails userDetails);
}
