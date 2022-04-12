package account.service;

import account.dto.PaymentDto;
import account.entity.Payment;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface PaymentService {

    void addPayment(List<Payment> request);

    boolean checkUniquePayment(Payment payment);

    void updatePaymentByEmployeePeriod(Payment payment);

    List<PaymentDto> getInfoUserByPeriod(String period, UserDetails userDetails);

    public Payment convertDtoPaymentToEntity(PaymentDto dto);

    public PaymentDto convertEntityPaymentToDto(Payment entity);
}
