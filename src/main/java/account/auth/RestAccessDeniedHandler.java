package account.auth;

import account.service.Event;
import account.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    LogService logService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logService.addLogFailedAuth(Event.ACCESS_DENIED, request.getUserPrincipal().getName(), request.getRequestURI());
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied!");
    }
}
