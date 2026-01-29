package com.apirest.vuttr.validations.validators;

import com.apirest.vuttr.repositories.UserRepository;
import com.apirest.vuttr.validations.anotations.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if(email == null || email.isBlank()) {
            return true;
        }
        return !userRepository.existsByEmail(email);
    }

}
