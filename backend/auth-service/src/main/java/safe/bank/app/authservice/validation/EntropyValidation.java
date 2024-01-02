package safe.bank.app.authservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EntropyValidator.class)
public @interface EntropyValidation {
    String message() default "The password is too weak.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
