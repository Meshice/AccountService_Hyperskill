package account.service;

import account.dto.LogDto;
import account.entity.Log;
import account.entity.User;
import account.request.LockUnlockUserRequest;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface LogService {

    public void addLogSignUp(Event eventName, User user, HttpServletRequest request);

    public void addLogChangePassword(Event event, UserDetails userDetails, HttpServletRequest request);

    public void addLogAdmin(Event event, UserDetails userDetails, String newRole, String updatedUserEmail, HttpServletRequest request);

    public void addLogFailedAuth(Event event, String userEmail, String object);

    public List<Log> getLogsOrderById();

    public void addLogUnlockLock(Event event, String subject, String object, HttpServletRequest request);

}
