package account.controller;


import account.dto.PaymentDto;
import account.service.LogService;
import account.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping("/api/empl")
@Validated
public class EmployeeController {

    @Autowired
    PaymentService paymentService;

    @Autowired
    LogService logService;

    @GetMapping("/payment")
    public ResponseEntity<List<PaymentDto>> getPayment(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) @Pattern(regexp = "(0[1-9]|1[0-2])-\\d\\d\\d\\d", message = "Wrong date!") String period) {

        List<PaymentDto> payments = paymentService.getInfoUserByPeriod(period, userDetails);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }




}


