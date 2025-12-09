package com.apiRest.VUTTR.validations.validators;

import com.apiRest.VUTTR.dtos.UserRegisterDTO;
import com.apiRest.VUTTR.validations.anotations.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserRegisterDTO> {

    @Override
    public boolean isValid(UserRegisterDTO dto, ConstraintValidatorContext constraintValidatorContext) {
        if(dto.password() == null || dto.confirmPassword() == null) {
            return false;
        }

        return dto.password().equals(dto.confirmPassword());
    }
}
