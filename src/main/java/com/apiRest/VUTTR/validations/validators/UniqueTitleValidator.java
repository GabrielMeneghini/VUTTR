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
    public boolean isValid(String title, ConstraintValidatorContext constraintValidatorContext) {
        if (title == null || title.isBlank()) {
            return true;
        }
        return !toolRepository.existsByTitleIgnoreCase(title);
    }

}
