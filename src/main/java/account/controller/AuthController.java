package account.controller;

import account.entity.User;
import account.request.PasswordChangeRequest;
import account.response.PasswordChangeSuccessResponse;
import account.service.Event;
import account.service.LogService;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    LogService logService;

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@Valid @RequestBody User user, BindingResult bindingResult, HttpServletRequest request) {
        User newUser = userService.signUp(user, bindingResult);
        logService.addLogSignUp(Event.CREATE_USER, user, request);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PostMapping("/changepass")
    public ResponseEntity<PasswordChangeSuccessResponse> changePass(@Valid @RequestBody PasswordChangeRequest changeRequest, BindingResult bindingResult
            , @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        ResponseEntity<PasswordChangeSuccessResponse> response = userService.changePassword(changeRequest, userDetails, bindingResult);
        logService.addLogChangePassword(Event.CHANGE_PASSWORD, userDetails, request);
        return response;
    }

}
