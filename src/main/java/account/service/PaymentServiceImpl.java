package account.service;

import account.dto.PaymentDto;
import account.entity.Payment;
import account.entity.User;
import account.userDAO.PaymentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentDAO paymentDAO;

    @Autowired
    UserService userService;

    @Override
    public void addPayment(List<Payment> request) {
        paymentDAO.addPayment(request);
    }

    @Override
    public boolean checkUniquePayment(Payment payment) {
        return paymentDAO.findEmailPeriodUnique(payment);
    }

    @Override
    public void updatePaymentByEmployeePeriod(Payment payment) {
        paymentDAO.updatePaymentByEmployeePeriod(payment);
    }

    @Override
    public List<PaymentDto> getInfoUserByPeriod(String period, UserDetails userDetails) {
        List<Payment> payments = paymentDAO.getInfoUserByPeriod(period, userDetails.getUsername());
        if (payments.size() == 0) {
            return Collections.emptyList();
        }
        User user = findByEmailIgnoreCase(userDetails.getUsername());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found!");
        }
        return preparePaymentDto(payments, user);
    }

    private List<PaymentDto> preparePaymentDto(List<Payment> payments, User user) {
        String name = user.getName();
        String lastname = user.getLastname();

        return payments.stream()
                .sorted(Comparator.comparing(Payment::getPeriod).reversed())
                .map(this::convertEntityPaymentToDto)
                .peek(paymentDto -> {
                    paymentDto.setName(name);
                    paymentDto.setLastname(lastname);
                    String salary = paymentDto.getSalary();
                    paymentDto.setSalary(salary.strip().replaceAll("^0*","") + " dollar(s)");
                }).collect(Collectors.toList());
    }


    public Payment convertDtoPaymentToEntity(PaymentDto dto) {
        Payment payment = new Payment();
        payment.setEmployee(dto.getEmployee());
        payment.setSalary(Long.parseLong(dto.getSalary()));
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dto.getPeriod());
            payment.setPeriod(date);
        } catch (ParseException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return payment;
    }

    public PaymentDto convertEntityPaymentToDto(Payment entity) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setEmployee(entity.getEmployee());
        paymentDto.setSalary(entity.getSalary().toString());
        paymentDto.setPeriod(new SimpleDateFormat("MMMMM-yyyy", Locale.ENGLISH).format(entity.getPeriod()));
        return paymentDto;
    }


    private User findByEmailIgnoreCase(String email) {
        return userService.findUserByUsername(email);
    }
}
