package account.userDAO;

import account.entity.Payment;

import java.util.List;

public interface PaymentDAO {


    void addPayment(List<Payment> payment);

    boolean findEmailPeriodUnique(Payment payment);

    void updatePaymentByEmployeePeriod(Payment payment);

    List<Payment> getInfoUserByPeriod(String period, String employee);
}
