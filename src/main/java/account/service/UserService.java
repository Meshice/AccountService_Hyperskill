package account.service;

import account.entity.User;
import account.request.LockUnlockUserRequest;
import account.request.UserRoleChangeRequest;
import account.response.PasswordChangeSuccessResponse;
import org.springframework.security.core.userdetails.UserDetails;

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
