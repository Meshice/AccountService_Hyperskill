package account.service;

import account.dto.DtoMarker;
import account.entity.EntityMarker;
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

    void update(User user);

    User signUp(User user);

    User findUserByUsername(String username);

    PasswordChangeSuccessResponse changePassword(String newPassword, UserDetails userDetails);

    boolean checkUserExist(String user);


    List<User> getAllUsersOrderById();

    void deleteUserByEmail(String email);

    User changeUserRole(UserRoleChangeRequest request);

    void lockUnlockUser(LockUnlockUserRequest request);

}
