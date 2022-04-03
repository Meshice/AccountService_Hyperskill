package account.auth;

import account.entity.User;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BruteForceProtectionService {

    @Autowired
    UserService userService;

    public void registerLoginFailure(String username) {
        User user = findUserByUsername(username);
        if (user != null && !user.isLocked() && !user.getRoles().contains("ROLE_ADMINISTRATOR")) {
            int failureCount = user.getAttemptsForLogging();
            if (failureCount - 1 > 0) {
                user.setAttemptsForLogging(--failureCount);
            } else {
                user.setLocked(true);
            }
            userService.update(user);
        }
    }

    private User findUserByUsername(String username) {
        return userService.findUserByUsername(username);
    }

    public void resetBruteForceCounter(String username) {
        User user = findUserByUsername(username);
        if (user != null) {
            user.setAttemptsForLogging(5);
            user.setLocked(false);
            userService.update(user);
        }
    }
}
