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
    UserDAO userDAO;

    @Autowired
    LogService logService;

    @Autowired
    HttpServletRequest request;


    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String email = event.getAuthentication().getName();
        User user = userDAO.findByEmailIgnoreCase(email);
        if (user == null) {
            logService.addLogFailedAuth(Event.LOGIN_FAILED, email, request.getRequestURI());
            return;
        }
        if (user.getAttemptsForLogging() <= 1) {
            if (user.getRole().equals("ROLE_ADMINISTRATOR")) {
                return;
            }
            userDAO.lockUnlockUser(email,"LOCK");
            userDAO.decreaseAttempt(email);
            logService.addLogFailedAuth(Event.LOGIN_FAILED, email, request.getRequestURI());
            logService.addLogUnlockLock(Event.BRUTE_FORCE, email, request.getRequestURI(), request);
            logService.addLogUnlockLock(Event.LOCK_USER, email, String.format("Lock user %s",email.toLowerCase()), request);
        } else {
            userDAO.decreaseAttempt(email);
            logService.addLogFailedAuth(Event.LOGIN_FAILED, email, request.getRequestURI());
        }
    }
}
