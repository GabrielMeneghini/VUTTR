package com.apirest.vuttr.validations.validators;

import com.apirest.vuttr.repositories.ToolRepository;
import com.apirest.vuttr.validations.anotations.UniqueLink;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueLinkValidator implements ConstraintValidator<UniqueLink, String> {

    @Autowired
    private ToolRepository toolRepository;

    @Override
    public boolean isValid(String link, ConstraintValidatorContext constraintValidatorContext) {
        if(link == null || link.isBlank()) {
            return true;
        }
        return !toolRepository.existsByLink(link);
    }

}
