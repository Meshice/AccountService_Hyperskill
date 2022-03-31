package account.auth;

import account.entity.User;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BruteForceProtectionService {

    @Autowired
    UserService userService;

    public void registerLoginFailure(String username) {
        User user = findUserByUsername(username);
        if (user != null && !user.isLocked()) {
            int failureCount = user.getAttemptsForLogging();
            if (failureCount - 1 > 0) {

            }
        }
    }

    private User findUserByUsername(String username) {
        return userService.findUserByUsername(username);
    }
}
