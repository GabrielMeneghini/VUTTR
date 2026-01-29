package com.apirest.vuttr.validations.validators;

import com.apirest.vuttr.validations.PasswordMatchesInterface;
import com.apirest.vuttr.validations.anotations.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordMatchesInterface> {

    @Override
    public boolean isValid(PasswordMatchesInterface pmi, ConstraintValidatorContext constraintValidatorContext) {
        if(pmi.getPassword() == null || pmi.getConfirmPassword() == null) {
            return false;
        }

        return pmi.getPassword().equals(pmi.getConfirmPassword());
    }

}
