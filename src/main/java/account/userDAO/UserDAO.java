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

    void update(User user);

    User findByEmailIgnoreCase(String email);

    List<User> getAllUsersOrderById();

    void deleteUser(String email);

    void lockUnlockUser(String email, String operation);

}
