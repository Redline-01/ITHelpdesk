package com.example.helpdesk.validations;

import com.example.helpdesk.dto.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterRequest> {

    @Override
    public boolean isValid(RegisterRequest dto, ConstraintValidatorContext context){
        if (dto.getPassword() == null || dto.getConfirmPassword() == null) {
            return true;
        }
        return dto.getPassword().equals(dto.getConfirmPassword());
    }

}
