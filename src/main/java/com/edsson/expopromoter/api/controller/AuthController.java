package com.edsson.expopromoter.api.controller;
import com.edsson.expopromoter.api.context.UserContext;
import com.edsson.expopromoter.api.core.service.JwtUtil;
import com.edsson.expopromoter.api.exceptions.EntityAlreadyExistException;
import com.edsson.expopromoter.api.exceptions.FailedToLoginException;
import com.edsson.expopromoter.api.exceptions.FailedToRegisterException;
import com.edsson.expopromoter.api.exceptions.RequestValidationException;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.GenericResponse;
import com.edsson.expopromoter.api.model.json.JsonUser;
import com.edsson.expopromoter.api.request.LoginRequest;
import com.edsson.expopromoter.api.request.RegisterDeviceRequest;
import com.edsson.expopromoter.api.request.RegistrationRequest;
import com.edsson.expopromoter.api.service.LoginService;
import com.edsson.expopromoter.api.service.RoleService;
import com.edsson.expopromoter.api.service.UserService;
import com.edsson.expopromoter.api.validator.LoginRequestValidator;
import com.edsson.expopromoter.api.validator.UserRegistrationRequestValidator;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import javassist.NotFoundException;
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
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/auth")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Api(value = "auth", description = "Auth controller")
public class AuthController {

//    final static Logger logger = Logger.getLogger(AuthController.class);

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
                                 HttpServletResponse response) throws FailedToRegisterException, RequestValidationException, EntityAlreadyExistException {
//        logger.info("Call controller method: /registration ");
        userRegistrationRequestValidator.validate(registrationRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        // Create user


        userService.create(registrationRequest);

        return toKenForUser(registrationRequest.getEmail(), response);
    }

    public JsonUser toKenForUser(String name, HttpServletResponse response) throws FailedToRegisterException {
        UserContext context = userService.getUser(name);
        if (context != null) {
            try {
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
                          HttpServletRequest request) throws RequestValidationException, FailedToLoginException {

        String ipAddress = request.getRemoteAddr();
        loginRequestValidator.validate(loginRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        UserContext userContext = loginService.login(loginRequest, ipAddress);
        String token = jwtService.tokenFor(userContext);
        response.setHeader("Token", token);
        return JsonUser.from(userContext);
    }

    @CrossOrigin
    @RequestMapping(
            value = "/device_register",
            method = POST,
            produces = APPLICATION_JSON_VALUE,
            consumes = {APPLICATION_JSON_VALUE}
    )
    public JsonUser registerDevice(@RequestBody RegisterDeviceRequest registerDeviceRequest,
                                   BindingResult bindingResult,
                                   HttpServletResponse response,
                                   HttpServletRequest request) throws FailedToRegisterException, EntityAlreadyExistException {


        userService.create(registerDeviceRequest);
        return toKenForUser(registerDeviceRequest.getDeviceId(), response);
    }

    @CrossOrigin
    @RequestMapping(
            value = "/device_login",
            method = POST,
            produces = APPLICATION_JSON_VALUE,
            consumes = {APPLICATION_JSON_VALUE}
    )
    public JsonUser loginDevice(@RequestBody RegisterDeviceRequest registerDeviceRequest,
                                BindingResult bindingResult,
                                HttpServletResponse response,
                                HttpServletRequest request) throws NotFoundException {
        User user = userService.findOneByEmail(registerDeviceRequest.getDeviceId());
        if (user != null) {
            UserContext userContext = UserContext.create(user);
            String token = jwtService.tokenFor(userContext);
            response.setHeader("Token", token);
            return JsonUser.from(userContext);
        } else throw new NotFoundException("Device " + registerDeviceRequest.getDeviceId() + "not exist in Database!");
    }


    @CrossOrigin
    @RequestMapping(value = "/reset_password", method = GET, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public GenericResponse resetPassword(HttpServletRequest request) {
        userService.resetPassword((User) request.getAttribute("user"));
        return new GenericResponse("Ok", new String[]{});
    }
}
