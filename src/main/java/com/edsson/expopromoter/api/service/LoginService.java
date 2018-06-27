package com.edsson.expopromoter.api.service;

import com.edsson.expopromoter.api.context.UserContext;
import com.edsson.expopromoter.api.exceptions.FailedToLoginException;
import com.edsson.expopromoter.api.exceptions.NoSuchUserException;
import com.edsson.expopromoter.api.exceptions.PasswordIncorrectException;
import com.edsson.expopromoter.api.exceptions.PermissionsNotEnoughException;
import com.edsson.expopromoter.api.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LoginService {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public LoginService(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserContext login(LoginRequest credentials, String ip) throws FailedToLoginException, PasswordIncorrectException, NoSuchUserException {
        UserContext userContext = userService.getUser(credentials.getEmail());

        if (userContext == null) {
//            logger.warn("UserDAO tried to login with un-existing login. UserLogin: " + credentials.getLogin());
            throw new FailedToLoginException(credentials.getEmail());
        }
        if (!bCryptPasswordEncoder.matches(credentials.getPassword(), userContext.getPasswordHash())) {
            //Todo: BLOCK USER Functionality MAY BE HERE
            throw new PasswordIncorrectException();
        }
        return userContext;
    }

    public UserContext adminLogin(LoginRequest credentials, String ip) throws FailedToLoginException, PasswordIncorrectException, PermissionsNotEnoughException, NoSuchUserException {
        if (userService.getUser(credentials.getEmail()).getRole().getRole().equals("ROLE_ADMIN")) {
            UserContext userContext = userService.getUser(credentials.getEmail());

            if (userContext == null) {
//            logger.warn("UserDAO tried to login with un-existing login. UserLogin: " + credentials.getLogin());
                throw new FailedToLoginException(credentials.getEmail());
            }
            if (!bCryptPasswordEncoder.matches(credentials.getPassword(), userContext.getPasswordHash())) {
                //Todo: BLOCK USER Functionality MAY BE HERE
                throw new PasswordIncorrectException();
            }
            return userContext;
        }
        else {
            throw new PermissionsNotEnoughException();
        }
    }

}
