package account.userDAO;

import account.entity.Payment;
import account.entity.User;
import account.request.UpdatePaymentRequest;
import account.response.PasswordChangeSuccessResponse;
import account.response.PaymentUserInfo;
import org.springframework.http.ResponseEntity;

import java.sql.ResultSet;
import java.util.List;

public interface UserDAO {

    void save(User user);

    User findByEmailIgnoreCase(String email);

    List<User> getAllUsersOrderById();

    int getUserCount();

    public void deleteUser(String email);

    void changePassword(int id, String newPassword);

    void addPayment(List<Payment> payment);

    boolean findEmailPeriodUnique(Payment payment);

    public void updatePaymentByEmployeePeriod(UpdatePaymentRequest payment);

    public List<PaymentUserInfo> getInfoUserByPeriod(String period, String employee);

    public void updatePersonRoles(User user);

    public void lockUnlockUser(String email, String operation);

    public void decreaseAttempt(String email);

    public void returnUserAttempt(String email);
}
