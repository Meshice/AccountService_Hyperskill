package account.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidator implements ConstraintValidator<DateValid, String> {

    String pattern;

    @Override
    public void initialize(DateValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            dateFormat.setLenient(false);
            dateFormat.parse(value);
            return true;
        }catch (ParseException ex) {
            return false;
        }
    }
}
