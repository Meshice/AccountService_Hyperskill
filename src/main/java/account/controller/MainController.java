package account.controller;


import account.entity.Log;
import account.entity.Payment;
import account.entity.User;
import account.request.LockUnlockUserRequest;
import account.request.PasswordChangeRequest;
import account.request.UpdatePaymentRequest;
import account.request.UserRoleChangeRequest;
import account.response.PasswordChangeSuccessResponse;
import account.response.PaymentAddSuccessResponse;
import account.response.UserDeleteSuccessResponse;
import account.service.Event;
import account.service.LogService;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
public class MainController {

    @Autowired
    UserService userService;

    @Autowired
    LogService logService;

    @PostMapping("/auth/signup")
    public ResponseEntity<User> signUp(@Valid @RequestBody User user, BindingResult bindingResult, HttpServletRequest request) {
        User newUser = userService.signUp(user, bindingResult);
        logService.addLogSignUp(Event.CREATE_USER, user, request);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PostMapping("/auth/changepass")
    public ResponseEntity<PasswordChangeSuccessResponse> changePass(@Valid @RequestBody PasswordChangeRequest changeRequest, BindingResult bindingResult
            , @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        ResponseEntity<PasswordChangeSuccessResponse> response = userService.changePassword(changeRequest, userDetails, bindingResult);
        logService.addLogChangePassword(Event.CHANGE_PASSWORD, userDetails, request);
        return response;
    }

    @PostMapping("/acct/payments")
    public ResponseEntity<PaymentAddSuccessResponse> addPayments(@RequestBody(required = false) List<@Valid Payment> payments,
                                                                 @AuthenticationPrincipal UserDetails userDetails) {
        return userService.addPayment(payments);
    }

    @PutMapping("/acct/payments")
    public ResponseEntity<PaymentAddSuccessResponse> updatePayment(@RequestBody @Valid UpdatePaymentRequest payment, @AuthenticationPrincipal UserDetails userDetails) {
        userService.updatePaymentByEmployeePeriod(payment);
        return new ResponseEntity<>(new PaymentAddSuccessResponse("Updated successfully!"), HttpStatus.OK);
    }

    @GetMapping("/empl/payment")
    public ResponseEntity getPayment(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) @Pattern(regexp = "(0[1-9]|1[0-2])-\\d\\d\\d\\d", message = "Wrong date!") String period) {
        return userService.getInfoUserByPeriod(period, userDetails);
    }

    @GetMapping("/admin/user")
    public ResponseEntity<List<User>> getAllUsers(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(userService.getAllUsersOrderById(), HttpStatus.OK);
    }

    @DeleteMapping("/admin/user/{email}")
    public ResponseEntity<UserDeleteSuccessResponse> deleteUser(@PathVariable(value = "email", required = false) String email,
                                                                @AuthenticationPrincipal UserDetails userDetails,
                                                                HttpServletRequest httpServletRequest) {
        userService.deleteUserByEmail(email);
        logService.addLogAdmin(Event.DELETE_USER, userDetails, null, email, httpServletRequest);
        return new ResponseEntity<>(new UserDeleteSuccessResponse(email, "Deleted successfully!"), HttpStatus.OK);
    }

    @PutMapping("/admin/user/role")
    public ResponseEntity<User> changeUserRole(@AuthenticationPrincipal UserDetails userDetails,
                                               @Valid @RequestBody UserRoleChangeRequest request, BindingResult bindingResult,
                                               HttpServletRequest httpServletRequest) {
        User user = userService.changeUserRole(request);
        logService.addLogAdmin(Event.valueOf(request.getOperation() + "_ROLE"), userDetails, request.getRole(), request.getUser(), httpServletRequest);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/admin/user/access")
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

    @GetMapping("/security/events")
    public ResponseEntity<List<Log>> getLogs(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(logService.getLogsOrderById(), HttpStatus.OK);
    }


}


