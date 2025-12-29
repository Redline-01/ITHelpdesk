package com.example.helpdesk.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = TicketStatusValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTicketStatus {

    String message() default "Status i pavlefshem i tiketes";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
