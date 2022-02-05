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

    User findByEmailIgnoreCase(String email);

    void save(User user);

    void changePassword(int id, String newPassword);

    void addPayment(List<Payment> payment);

    boolean findEmailPeriodUnique(Payment payment);

    int findUserByEmail(String email);

    public void updatePaymentByEmployeePeriod(UpdatePaymentRequest payment);

    public List<PaymentUserInfo> getInfoUserByPeriod(String period, String employee);

    int getUserCount();

    List<User> getAllUsersOrderById();

    public void deleteUser(String email);

    public void updatePersonRoles(User user);

    public void lockUnlockUser(String email, String operation);

    public void decreaseAttempt(String email);

    public void returnUserAttempt(String email);
}
