package account.controller;

import account.dto.UserDto;
import account.entity.User;
import account.request.PasswordChangeRequest;
import account.response.PasswordChangeSuccessResponse;
import account.service.Event;
import account.service.LogService;
import account.util.MappingUtils;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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

    @Autowired
    MappingUtils mapper;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody @Valid UserDto user, BindingResult bindingResult, HttpServletRequest request) {
        User userEntity = mapper.convertDtoToEntity(user, User.class);

        UserDto newUserDto = mapper.convertEntityToDto(userService.signUp(userEntity),UserDto.class);
        logService.addLogSignUp(Event.CREATE_USER, userEntity, request);

        return new ResponseEntity<>(newUserDto, HttpStatus.OK);
    }

    @PostMapping("/changepass")
    public ResponseEntity<PasswordChangeSuccessResponse> changePass(@Valid @RequestBody PasswordChangeRequest changeRequest, BindingResult bindingResult
                                                                    ,@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        PasswordChangeSuccessResponse response = userService.changePassword(changeRequest.getNewPassword(), userDetails);

        logService.addLogChangePassword(Event.CHANGE_PASSWORD, userDetails, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
