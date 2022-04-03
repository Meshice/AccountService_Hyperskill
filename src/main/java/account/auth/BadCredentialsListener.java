package account.auth;

import account.entity.User;
import account.request.LockUnlockUserRequest;
import account.service.Event;
import account.service.LogService;
import account.service.UserService;
import account.userDAO.LogDAO;
import account.userDAO.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@Component
public class BadCredentialsListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    BruteForceProtectionService bruteForceProtectionService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();
        bruteForceProtectionService.registerLoginFailure(username);
//        logService.addLogFailedAuth(Event.LOGIN_FAILED, username, request.getRequestURI());
//        logService.addLogUnlockLock(Event.BRUTE_FORCE, username, request.getRequestURI(), request);
//        logService.addLogUnlockLock(Event.LOCK_USER, username, String.format("Lock user %s",username.toLowerCase()), request);

    }
}
