package account.annotation;

import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmployeeExistValidator implements ConstraintValidator<EmployeeExist, String> {

    @Autowired
    UserService userService;

    @Override
    public boolean isValid(String employee, ConstraintValidatorContext context) {
        return userService.checkUserExist(employee);
    }

    @Override
    public void initialize(EmployeeExist constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
