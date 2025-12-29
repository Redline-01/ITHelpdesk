package com.example.helpdesk.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class TicketStatusValidator implements ConstraintValidator<ValidTicketStatus, String> {

    private static final Set<String> ALLOWED =
            Set.of("OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        return value != null && ALLOWED.contains(value);
    }
}
