package com.edsson.expopromoter.api.controller;

import com.edsson.expopromoter.api.context.Messages;
import com.edsson.expopromoter.api.context.UserContext;
import com.edsson.expopromoter.api.core.service.JwtUtil;
import com.edsson.expopromoter.api.exceptions.*;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.GenericResponse;
import com.edsson.expopromoter.api.model.json.JsonUser;
import com.edsson.expopromoter.api.operator.MailSender;
import com.edsson.expopromoter.api.request.*;
import com.edsson.expopromoter.api.service.LoginService;
import com.edsson.expopromoter.api.service.RoleService;
import com.edsson.expopromoter.api.service.UserService;
import com.edsson.expopromoter.api.validator.*;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import javassist.NotFoundException;
import org.apache.log4j.Logger;
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
    final static Logger logger = Logger.getLogger(AuthController.class);

    private final UserRegistrationRequestValidator userRegistrationRequestValidator;
    private final UserService userService;
    private final RoleService roleService;
    private final JwtUtil jwtService;
    private final LoginRequestValidator loginRequestValidator;
    private final LoginService loginService;
    private final ResetPasswordValidator resetPasswordValidator;
    private final MailSender mailSender;
    private final UpdatePasswordRequestValidator updatePasswordRequestValidator;
    private final UpdateEmailRequestValidator updateEmailRequestValidator;

    @Autowired
    public AuthController(UpdateEmailRequestValidator updateEmailRequestValidator, UpdatePasswordRequestValidator updatePasswordRequestValidator, MailSender mailSender, ResetPasswordValidator resetPasswordValidator, LoginService loginService, LoginRequestValidator loginRequestValidator, UserRegistrationRequestValidator userRegistrationRequestValidator, UserService userService, RoleService roleService, JwtUtil jwtService) {
        this.userRegistrationRequestValidator = userRegistrationRequestValidator;
        this.loginService = loginService;
        this.userService = userService;
        this.roleService = roleService;
        this.jwtService = jwtService;
        this.loginRequestValidator = loginRequestValidator;
        this.resetPasswordValidator = resetPasswordValidator;
        this.mailSender = mailSender;
        this.updatePasswordRequestValidator = updatePasswordRequestValidator;
        this.updateEmailRequestValidator = updateEmailRequestValidator;
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
        logger.info("Call controller method: /registration ");
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


    @RequestMapping(
            value = "/reset_password",
            method = POST,
            produces = APPLICATION_JSON_VALUE,
            consumes = {APPLICATION_JSON_VALUE, TEXT_PLAIN_VALUE}
    )
    public GenericResponse resetPassword(@RequestBody ResetPasswordRequest credentials, BindingResult bindingResult) throws Exception {
        String token = sendUpdateMessage(credentials, bindingResult);


        mailSender.operateMessage(credentials.getEmail(), token, true);
        return new GenericResponse(Messages.MESSAGE_PASSWORD_RESET_SUCCESS, new String[]{credentials.getEmail()});
    }

    @RequestMapping(
            value = "/update_password",
            method = POST,
            produces = APPLICATION_JSON_VALUE,
            consumes = {APPLICATION_JSON_VALUE, TEXT_PLAIN_VALUE}
    )
    public GenericResponse updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest,
                                          BindingResult bindingResult) throws Exception {
        updatePasswordRequestValidator.validate(updatePasswordRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }

        UserContext user = userService.findUserByResetPasswordToken(updatePasswordRequest.getUpdatePasswordToken());
        if (user == null) {
            throw new NoSuchUserException("No user for this reset password token exists");
        }
        userService.updateUserPassword(user, updatePasswordRequest.getNewPassword(), updatePasswordRequest.getUpdatePasswordToken());
        logger.info("Password updated for user: " + user.getUserId());
        return new GenericResponse(Messages.MESSAGE_PASSWORD_UPDATE_SUCCESS, new String[]{user.getEmail()});
    }


    @RequestMapping(value = "/reset_email", method = POST, produces = APPLICATION_JSON_VALUE, consumes = {APPLICATION_JSON_VALUE, TEXT_PLAIN_VALUE})
    public GenericResponse resetEmail(@RequestBody ResetPasswordRequest credentials,
                                      BindingResult bindingResult) throws Exception {

        String token = sendUpdateMessage(credentials, bindingResult);
        mailSender.operateMessage(credentials.getEmail(), token, false);
        return new GenericResponse(Messages.MESSAGE_EMAIL_RESET_SUCCESS, new String[]{credentials.getEmail()});
    }


    @RequestMapping(value = "/update_email", method = POST, produces = APPLICATION_JSON_VALUE, consumes = {APPLICATION_JSON_VALUE, TEXT_PLAIN_VALUE})
    public GenericResponse updateEmail(@RequestBody UpdateEmailRequest updateEmailRequest,
                                       BindingResult bindingResult) throws Exception {

        updateEmailRequestValidator.validate(updateEmailRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }

        UserContext user = userService.findUserByResetPasswordToken(updateEmailRequest.getUpdateEmailToken());
        if (user == null) {
            throw new NoSuchUserException("No user for this reset password token exists");
        }
        userService.updateUserEmail(user,updateEmailRequest.getNewEmail(),updateEmailRequest.getUpdateEmailToken());
        logger.info("Password updated for user: " + user.getUserId());
        return new GenericResponse(Messages.MESSAGE_PASSWORD_UPDATE_SUCCESS, new String[]{user.getEmail()});
    }


    private String sendUpdateMessage(ResetPasswordRequest credentials, BindingResult bindingResult) throws RequestValidationException, NoSuchUserException, FailedToCreateResetPasswordTokenException {
        resetPasswordValidator.validate(credentials, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }

        String userEmail = credentials.getEmail();
        User user = userService.findOneByEmail(userEmail);
        if (user == null) {
            throw new NoSuchUserException(userEmail);
        }
        String token = userService.createPasswordResetTokenForUser(user);

        logger.info("START MAILSERVICE");
        return token;
    }
}
