package account.annotation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmployeeExistValidator.class)
public @interface EmployeeExist {

    public String message() default "An employee must be among the users of our service!";

    public Class<?> [] groups() default {};

    public Class<? extends Payload> [] payload() default {};
}
