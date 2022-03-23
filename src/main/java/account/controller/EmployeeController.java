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
@RequestMapping("/api/empl")
@Validated
public class EmployeeController {

    @Autowired
    UserService userService;

    @Autowired
    LogService logService;

    @GetMapping("/empl/payment")
    public ResponseEntity<?> getPayment(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) @Pattern(regexp = "(0[1-9]|1[0-2])-\\d\\d\\d\\d", message = "Wrong date!") String period) {
        return userService.getInfoUserByPeriod(period, userDetails);
    }




}


