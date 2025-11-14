package com.apiRest.VUTTR.validations.anotations;

import com.apiRest.VUTTR.validations.validators.UniqueEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {

    String message() default "already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
