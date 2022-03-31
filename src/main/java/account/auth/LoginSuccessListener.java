package account.auth;

import account.service.UserService;
import account.userDAO.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    UserDAO userDAO;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        userDAO.returnUserAttempt(event.getAuthentication().getName());
    }
}
