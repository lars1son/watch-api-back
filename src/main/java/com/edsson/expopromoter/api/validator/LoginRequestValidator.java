package com.edsson.expopromoter.api.validator;

import com.edsson.expopromoter.api.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class LoginRequestValidator implements Validator {

    @Autowired
    public LoginRequestValidator() {
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return LoginRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
    }
}
