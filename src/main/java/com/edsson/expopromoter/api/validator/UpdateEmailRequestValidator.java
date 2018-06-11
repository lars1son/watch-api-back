package com.edsson.expopromoter.api.validator;

import com.edsson.expopromoter.api.request.UpdateEmailRequest;
import com.edsson.expopromoter.api.request.UpdatePasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.edsson.expopromoter.api.validator.ValidatorHelper.*;

@Component
public class UpdateEmailRequestValidator implements Validator {



    private Pattern emailPattern;


    @Autowired
    public UpdateEmailRequestValidator() {
        emailPattern = Pattern.compile(EMAIL_PATTERN);

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UpdateEmailRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UpdateEmailRequest updateEmailRequest = (UpdateEmailRequest) o;


        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "updateEmailToken", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newEmail", "NotEmpty");

        if (!errors.hasFieldErrors("email")) {
            String email =updateEmailRequest.getNewEmail();
            if (email.length() < MIN_LOGIN_SIZE || email.length() > MAX_LOGIN_SIZE) {
                errors.rejectValue("newEmail", "Size.userForm.email");
            }
            Matcher matcher = emailPattern.matcher(email);
            if (!matcher.matches()){
                errors.rejectValue("newEmail", "Pattern.userForm.email");
            }
        }
    }
}