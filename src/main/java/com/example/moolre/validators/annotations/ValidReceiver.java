package com.example.moolre.validators.annotations;

import com.example.moolre.validators.validator.ReceiverValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReceiverValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidReceiver {
    String message() default "Receiver must be a valid phone number or bank account number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
