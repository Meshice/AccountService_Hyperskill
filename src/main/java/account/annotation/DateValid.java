package account.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface DateValid {

    public String message() default "Wrong date format!";
    public String pattern() default "yyyy-MM-dd";

    public Class<?> [] groups() default {};

    public Class<? extends Payload> [] payload() default {};
}
