package com.apiRest.VUTTR.validations.validators;

import com.apiRest.VUTTR.repositories.ToolRepository;
import com.apiRest.VUTTR.validations.anotations.UniqueLink;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueLinkValidator implements ConstraintValidator<UniqueLink, String> {

    @Autowired
    private ToolRepository toolRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null || s.isBlank()) {
            return true;
        }
        return !toolRepository.existsByLink(s);
    }

}
