package account.controller;

import account.dto.PaymentDto;
import account.entity.Payment;
import account.request.UpdatePaymentRequest;
import account.response.PaymentAddSuccessResponse;
import account.util.MappingUtils;
import account.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/acct")
@Validated
public class AccountantController {

    @Autowired
    PaymentService paymentService;

    @Autowired
    MappingUtils mapper;

    @PostMapping("/payments")
    public ResponseEntity<PaymentAddSuccessResponse> addPayments(@RequestBody @Size(min = 1) List<@Valid PaymentDto> paymentsDto, BindingResult br) {
        List<Payment> paymentsEntity = paymentsDto.stream()
                .map(payment -> mapper.convertDtoToEntity(payment, Payment.class))
                .collect(Collectors.toList());

        paymentService.addPayment(paymentsEntity);
        return ResponseEntity.ok(new PaymentAddSuccessResponse("Added successfully!"));
    }

    @PutMapping("/payments")
    public ResponseEntity<PaymentAddSuccessResponse> updatePayment(@RequestBody @Valid UpdatePaymentRequest payment) {

        paymentService.updatePaymentByEmployeePeriod(payment);
        return ResponseEntity.ok(new PaymentAddSuccessResponse("Updated successfully!"));
    }

}
