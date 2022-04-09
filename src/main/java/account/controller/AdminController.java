package account.controller;

import account.dto.UserDto;
import account.entity.User;
import account.request.LockUnlockUserRequest;
import account.request.UserRoleChangeRequest;
import account.response.UserDeleteSuccessResponse;
import account.service.Event;
import account.service.LogService;
import account.util.MappingUtils;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/admin")
@Validated
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    LogService logService;

    @Autowired
    MappingUtils mapper;

    @GetMapping("/user")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> usersDto = userService.getAllUsersOrderById().stream()
                .map(user -> mapper.convertEntityToDto(user, UserDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }

    @DeleteMapping("/user/{email}")
    public ResponseEntity<UserDeleteSuccessResponse> deleteUser(@PathVariable(value = "email", required = false) String email,
                                                                @AuthenticationPrincipal UserDetails userDetails,
                                                                HttpServletRequest httpServletRequest) {

        userService.deleteUserByEmail(email);
        logService.addLogAdmin(Event.DELETE_USER, userDetails, null, email, httpServletRequest);
        return new ResponseEntity<>(new UserDeleteSuccessResponse(email, "Deleted successfully!"), HttpStatus.OK);
    }

    @PutMapping("/user/role")
    public ResponseEntity<UserDto> changeUserRole(@AuthenticationPrincipal UserDetails userDetails,
                                               @Valid @RequestBody UserRoleChangeRequest request, BindingResult bindingResult,
                                               HttpServletRequest httpServletRequest) {

        User userEntity = userService.changeUserRole(request);
        UserDto userDto = mapper.convertEntityToDto(userEntity, UserDto.class);

        logService.addLogAdmin(Event.valueOf(request.getOperation() + "_ROLE"), userDetails, request.getRole(), request.getUser(), httpServletRequest);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping("/user/access")
    public ResponseEntity<Map<String, String>> unlockLockUser(@Valid @RequestBody LockUnlockUserRequest request,
                                                              @AuthenticationPrincipal UserDetails userDetails,
                                                              HttpServletRequest httpServletRequest) {
        userService.lockUnlockUser(request);
        if (request.getOperation().equals("LOCK"))
            logService.addLogUnlockLock(Event.LOCK_USER, userDetails.getUsername().toLowerCase(), String.format("Lock user %s",request.getUser().toLowerCase()), httpServletRequest);
        else
            logService.addLogUnlockLock(Event.UNLOCK_USER, userDetails.getUsername().toLowerCase(), String.format("Unlock user %s",request.getUser().toLowerCase()), httpServletRequest);
        return new ResponseEntity<>(Map.of("status", String.format("User %s %sed!", request.getUser().toLowerCase(), request.getOperation().toLowerCase())), HttpStatus.OK);
    }
}
