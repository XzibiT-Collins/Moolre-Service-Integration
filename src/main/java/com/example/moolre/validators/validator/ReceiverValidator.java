package com.example.moolre.validators.validator;

import com.example.moolre.validators.annotations.ValidReceiver;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReceiverValidator implements ConstraintValidator<ValidReceiver, String> {

    private static final String PHONE_REGEX = "^(\\+?[0-9]{10,15})$";
    private static final String BANK_ACCOUNT_REGEX = "^[0-9]{8,20}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return value.matches(PHONE_REGEX) || value.matches(BANK_ACCOUNT_REGEX);
    }
}

