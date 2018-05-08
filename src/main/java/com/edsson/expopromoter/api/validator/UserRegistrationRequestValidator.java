package com.edsson.expopromoter.api.validator;

import com.edsson.expopromoter.api.request.RegistrationRequest;
import com.edsson.expopromoter.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.edsson.expopromoter.api.validator.ValidatorHelper.*;




@Component
public class UserRegistrationRequestValidator implements Validator {


    private final UserService userService;



    private Pattern passwordPattern;

    @Autowired
    public UserRegistrationRequestValidator(UserService userService) {
        this.userService = userService;


        //Todo: Maybe password pattern functionality will appear
        passwordPattern =  Pattern.compile(PASSWORD_PATTERN);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return RegistrationRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RegistrationRequest registrationRequest = (RegistrationRequest) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
        String username = registrationRequest.getEmail();
        if (!errors.hasFieldErrors("email")){
            if (username.length() < MIN_LOGIN_SIZE || username.length() > MAX_LOGIN_SIZE) {
                errors.rejectValue("email", "Size.userForm.username");
            }
            if (userService.getUser(username)!=null) {
                errors.rejectValue("email", "Duplicate.userForm.username");
            }
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (!errors.hasFieldErrors("password")) {
            String password = registrationRequest.getPassword();
            if (password.length() < MIN_PASSWORD_SIZE || password.length() > MAX_PASSWORD_SIZE) {
                errors.rejectValue("password", "Size.userForm.password");
            }
            Matcher matcher = passwordPattern.matcher(password);
            if (!matcher.matches()){
                errors.rejectValue("password", "Pattern.userForm.password");
            }
        }
    }
}