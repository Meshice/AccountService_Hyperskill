package account.controller;

import account.entity.Payment;
import account.request.UpdatePaymentRequest;
import account.response.PaymentAddSuccessResponse;
import account.service.LogService;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/acct")
@Validated
public class AccountantController {

    @Autowired
    UserService userService;

    @PostMapping("/acct/payments")
    public ResponseEntity<PaymentAddSuccessResponse> addPayments(@RequestBody(required = false) List<@Valid Payment> payments) {
        return userService.addPayment(payments);
    }

    @PutMapping("/acct/payments")
    public ResponseEntity<PaymentAddSuccessResponse> updatePayment(@RequestBody @Valid UpdatePaymentRequest payment) {
        userService.updatePaymentByEmployeePeriod(payment);
        return new ResponseEntity<>(new PaymentAddSuccessResponse("Updated successfully!"), HttpStatus.OK);
    }

}
