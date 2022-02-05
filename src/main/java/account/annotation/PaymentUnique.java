package account.annotation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PaymentUniqueValidator.class)
public @interface PaymentUnique {

    public String message() default "The employee-period pair must be unique!";

    public Class<?> [] groups() default {};

    public Class<? extends Payload> [] payload() default {};
}
