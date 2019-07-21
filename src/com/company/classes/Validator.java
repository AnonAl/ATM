package com.company.classes;

import com.company.interfaces.IValidator;

public class Validator implements IValidator {
    @Override
    public boolean validate(String cardNumber, String regEx) {
        return cardNumber.matches(regEx);
    }
}
