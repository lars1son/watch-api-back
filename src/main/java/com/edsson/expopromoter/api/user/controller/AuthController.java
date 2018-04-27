package com.edsson.expopromoter.api.user.controller;


import com.edsson.expopromoter.api.config.RolesConfiguration;
import com.edsson.expopromoter.api.exceptions.FailedToRegisterException;
import com.edsson.expopromoter.api.exceptions.RequestValidationException;
import com.edsson.expopromoter.api.user.model.RegistrationRequest;
import com.edsson.expopromoter.api.user.model.User;
import com.edsson.expopromoter.api.user.service.UserService;
import com.edsson.expopromoter.api.validator.UserRegistrationRequestValidator;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@RequestMapping("/auth")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Api(value = "auth", description = "Auth controller")
public class AuthController {


    private final UserRegistrationRequestValidator userRegistrationRequestValidator;
    private final UserService userService;
    private final RolesConfiguration rolesConfiguration;

    @Autowired
    public AuthController(UserRegistrationRequestValidator userRegistrationRequestValidator, UserService userService, RolesConfiguration rolesConfiguration) {
        this.userRegistrationRequestValidator = userRegistrationRequestValidator;
        this.rolesConfiguration=rolesConfiguration;
        this.userService=userService;
    }


    @CrossOrigin
    @RequestMapping(
            value = "/registration",
            method = RequestMethod.POST,
            produces = APPLICATION_JSON_VALUE,
            consumes = {APPLICATION_JSON_VALUE, TEXT_PLAIN_VALUE}
    )
    public ResponseEntity<User> registration(@RequestBody RegistrationRequest registrationRequest,
                                             BindingResult bindingResult,
                                             HttpServletResponse response) throws FailedToRegisterException, RequestValidationException {

        userRegistrationRequestValidator.validate(registrationRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        // Create user

        userService.create(new User(registrationRequest.getEmail(),registrationRequest.getPassword(),   ));
        UserContext context = userService.getUser(registrationRequest.getLogin());
        if (context != null) {
            try {
                response.setHeader("Access-Token", jwtService.tokenFor(context));
                response.setHeader("Token", jwtService.tokenFor(context));
            } catch (JwtException e) {
                e.printStackTrace();
            }
            logger.info("Successfully registered user with login: " + context.getLogin());
            return User.from(context);
        } else {
            throw new FailedToRegisterException("UserDAO not created.");
        }
    }
}
