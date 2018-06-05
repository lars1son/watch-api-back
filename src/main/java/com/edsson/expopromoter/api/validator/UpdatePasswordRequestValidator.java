package com.edsson.expopromoter.api.validator;

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
public class UpdatePasswordRequestValidator implements Validator {



    private Pattern emailPattern;
//    private Pattern passwordPattern;

    @Autowired
    public UpdatePasswordRequestValidator() {
        emailPattern = Pattern.compile(EMAIL_PATTERN);
//        passwordPattern = Pattern.compile(PASSWORD_PATTERN);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UpdatePasswordRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UpdatePasswordRequest updatePasswordRequest = (UpdatePasswordRequest) o;


        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "updatePasswordToken", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "NotEmpty");

        if (!errors.hasFieldErrors("password")) {
            String password = updatePasswordRequest.getNewPassword();
            if (password.length() < MIN_PASSWORD_SIZE || password.length() > MAX_PASSWORD_SIZE) {
                errors.rejectValue("newPassword", "Size.userForm.password");
            }
//            Matcher matcher = passwordPattern.matcher(password);
//            if (!matcher.matches()){
//                errors.rejectValue("newPassword", "Pattern.userForm.password");
//            }
        }
    }
}
