package account.annotation;

import account.dto.PaymentDto;
import account.entity.Payment;
import account.service.PaymentService;
import account.userDAO.PaymentDAO;
import account.util.MappingUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PaymentUniqueValidator implements ConstraintValidator<PaymentUnique, PaymentDto> {

    @Autowired
    PaymentService service;

    @Autowired
    MappingUtils mapper;

    @Override
    public boolean isValid(PaymentDto value, ConstraintValidatorContext context) {
        return service.checkUniquePayment(mapper.convertDtoToEntity(value, Payment.class));
    }

    @Override
    public void initialize(PaymentUnique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
