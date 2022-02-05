package account.service;

import account.entity.Payment;
import account.entity.User;
import account.request.LockUnlockUserRequest;
import account.request.PasswordChangeRequest;
import account.request.UpdatePaymentRequest;
import account.request.UserRoleChangeRequest;
import account.response.PasswordChangeSuccessResponse;
import account.response.PaymentAddSuccessResponse;
import account.response.PaymentUserInfo;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface UserService {

    public User signUp(User user, BindingResult result);

    public User signIn(UserDetails userDetails);

    public ResponseEntity<PasswordChangeSuccessResponse> changePassword(PasswordChangeRequest changeRequest, UserDetails userDetails, BindingResult result);

    public ResponseEntity<PaymentAddSuccessResponse> addPayment(List<Payment> request);

    public boolean checkUniquePayment(Payment payment);

    public boolean checkUserExist(String user);

    public void updatePaymentByEmployeePeriod(UpdatePaymentRequest payment);

    public ResponseEntity getInfoUserByPeriod(String period, UserDetails userDetails);

    List<User> getAllUsersOrderById();

    public void deleteUserByEmail(String email);

    public User changeUserRole(UserRoleChangeRequest request);

    public void lockUnlockUser(LockUnlockUserRequest request);

    public void returnUserAttempt(String email);


}
