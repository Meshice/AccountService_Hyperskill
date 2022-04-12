package account.service;

import account.dto.LogDto;
import account.entity.Log;
import account.entity.User;
import account.userDAO.LogDAO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    LogDAO logDAO;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<Log> getLogsOrderById() {
        return logDAO.getLogsOrderById();
    }

    @Override
    public void addLogSignUp(Event eventName, User user, HttpServletRequest request) {
        Log log = new Log();
        log.setAction(eventName.toString());
        log.setSubject("Anonymous");
        log.setObject(user.getEmail().toLowerCase());
        log.setPath(request.getRequestURI());
        logDAO.addLog(log);
    }

    @Override
    public void addLogChangePassword(Event event, UserDetails userDetails, HttpServletRequest request) {
        Log log = new Log();
        log.setAction(event.toString());
        log.setSubject(userDetails.getUsername().toLowerCase());
        log.setObject(userDetails.getUsername().toLowerCase());
        log.setPath(request.getRequestURI());
        logDAO.addLog(log);
    }

    @Override
    public void addLogAdmin(Event event, UserDetails userDetails, String role, String updatedUserEmail, HttpServletRequest request) {
        Log log = new Log();
        log.setAction(event.toString());
        log.setSubject(userDetails.getUsername().toLowerCase());
        log.setPath(request.getRequestURI());
        switch (event) {
            case DELETE_USER:
                log.setObject(updatedUserEmail.toLowerCase());
                break;
            case GRANT_ROLE:
            case REMOVE_ROLE:
                log.setObject(String.format(event.getEventMessage(), role, updatedUserEmail.toLowerCase()));
                break;
        }
        logDAO.addLog(log);
    }

    @Override
    public void addLogFailedAuth(Event event, String userEmail, String object) {
        Log log = new Log();
        log.setAction(event.toString());
        log.setSubject(userEmail);
        log.setObject(object);
        log.setPath(object);
        logDAO.addLog(log);
    }

    @Override
    public void addLogUnlockLock(Event event, String subject, String object, HttpServletRequest request) {
        Log log = new Log();
        log.setAction(event.toString());
        log.setSubject(subject);
        log.setObject(object);
        log.setPath(request.getRequestURI());
        logDAO.addLog(log);
    }

}
