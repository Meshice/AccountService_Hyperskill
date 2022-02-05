package account.annotation;

import account.entity.Payment;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PaymentUniqueValidator implements ConstraintValidator<PaymentUnique, Payment> {

    @Autowired
    UserService userService;

    @Override
    public boolean isValid(Payment value, ConstraintValidatorContext context) {
        return userService.checkUniquePayment(value);
    }

    @Override
    public void initialize(PaymentUnique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
