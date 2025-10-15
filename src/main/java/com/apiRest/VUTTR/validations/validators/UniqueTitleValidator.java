package com.apiRest.VUTTR.validations.validators;

import com.apiRest.VUTTR.repositories.ToolRepository;
import com.apiRest.VUTTR.validations.anotations.UniqueTitle;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueTitleValidator implements ConstraintValidator<UniqueTitle, String> {

    @Autowired
    private ToolRepository toolRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return !toolRepository.existsByTitleIgnoreCase(value);
    }

}
