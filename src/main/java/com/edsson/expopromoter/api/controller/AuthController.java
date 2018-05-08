package com.edsson.expopromoter.api.controller;


import com.edsson.expopromoter.api.context.UserContext;
import com.edsson.expopromoter.api.core.service.JwtUtil;
import com.edsson.expopromoter.api.exceptions.FailedToRegisterException;
import com.edsson.expopromoter.api.exceptions.RequestValidationException;
import com.edsson.expopromoter.api.request.RegistrationRequest;
import com.edsson.expopromoter.api.model.json.JsonUser;
import com.edsson.expopromoter.api.request.LoginRequest;
import com.edsson.expopromoter.api.service.LoginService;
import com.edsson.expopromoter.api.service.RoleService;
import com.edsson.expopromoter.api.service.UserService;
import com.edsson.expopromoter.api.validator.LoginRequestValidator;
import com.edsson.expopromoter.api.validator.UserRegistrationRequestValidator;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/auth")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Api(value = "auth", description = "Auth controller")
public class AuthController {


    private final UserRegistrationRequestValidator userRegistrationRequestValidator;
    private final UserService userService;
    private final RoleService roleService;
    private final JwtUtil jwtService;
    private final LoginRequestValidator loginRequestValidator;
    private final LoginService loginService;

    @Autowired
    public AuthController(LoginService loginService, LoginRequestValidator loginRequestValidator, UserRegistrationRequestValidator userRegistrationRequestValidator, UserService userService, RoleService roleService, JwtUtil jwtService) {
        this.userRegistrationRequestValidator = userRegistrationRequestValidator;
        this.loginService = loginService;
        this.userService = userService;
        this.roleService = roleService;
        this.jwtService = jwtService;
        this.loginRequestValidator = loginRequestValidator;
    }


    @CrossOrigin
    @RequestMapping(
            value = "/registration",
            method = RequestMethod.POST,
            produces = APPLICATION_JSON_VALUE,
            consumes = {APPLICATION_JSON_VALUE, TEXT_PLAIN_VALUE}
    )
    public JsonUser registration(@RequestBody RegistrationRequest registrationRequest,
                                 BindingResult bindingResult,
                                 HttpServletResponse response) throws FailedToRegisterException, RequestValidationException {

        userRegistrationRequestValidator.validate(registrationRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        // Create user

        //Todo: create UserServiceImpl class for new level. With code below
//        RoleDAO roleDAO = roleService.findRoleDAOByRole(RolesConfiguration.ROLE_USER);
//        userService.create(new User(registrationRequest.getEmail(),registrationRequest.getPassword(), roleDAO  ));
        userService.create(registrationRequest);
        UserContext context = userService.getUser(registrationRequest.getEmail());
        if (context != null) {
            try {
                String token = jwtService.tokenFor(context);
                response.setHeader("Access-Token", token);
                response.setHeader("Token", jwtService.tokenFor(context));
            } catch (JwtException e) {
                e.printStackTrace();
            }
//            logger.info("Successfully registered user with login: " + context.getLogin());
            return JsonUser.from(context);
        } else {
            throw new FailedToRegisterException("UserDAO not created.");
        }
    }

    @CrossOrigin
    @RequestMapping(
            value = "/login",
            method = POST,
            produces = APPLICATION_JSON_VALUE,
            consumes = {APPLICATION_JSON_VALUE}
    )
    public JsonUser login(@RequestBody LoginRequest loginRequest,
                      BindingResult bindingResult,
                      HttpServletResponse response,
                      HttpServletRequest request) throws Exception {

        String ipAddress = request.getRemoteAddr();
        loginRequestValidator.validate(loginRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        UserContext userContext = loginService.login(loginRequest, ipAddress);
        String token = jwtService.tokenFor(userContext);
        response.setHeader("Token", token);

//        systemLog.logUserSuccessLogin(loginRequest.getLogin(), token);

        return JsonUser.from(userContext);
    }
}
