package com.apiRest.VUTTR.validations.anotations;

import com.apiRest.VUTTR.validations.validators.UniqueLinkValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueLinkValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueLink {

    String message() default "already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
