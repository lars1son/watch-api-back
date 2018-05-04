package com.edsson.expopromoter.api.controller;


import com.edsson.expopromoter.api.config.RolesConfiguration;
import com.edsson.expopromoter.api.context.UserContext;
import com.edsson.expopromoter.api.core.service.JwtUtil;
import com.edsson.expopromoter.api.exceptions.FailedToRegisterException;
import com.edsson.expopromoter.api.exceptions.RequestValidationException;
import com.edsson.expopromoter.api.model.RegistrationRequest;
import com.edsson.expopromoter.api.model.RoleDAO;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.JsonUser;
import com.edsson.expopromoter.api.service.RoleService;
import com.edsson.expopromoter.api.service.UserService;
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

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@RequestMapping("/auth")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Api(value = "auth", description = "Auth controller")
public class AuthController {


    private final UserRegistrationRequestValidator userRegistrationRequestValidator;
    private final UserService userService;
    private final RoleService roleService;
    private final JwtUtil jwtService;


    @Autowired
    public AuthController(UserRegistrationRequestValidator userRegistrationRequestValidator, UserService userService, RoleService roleService, JwtUtil jwtService) {
        this.userRegistrationRequestValidator = userRegistrationRequestValidator;
//        this.rolesConfiguration=rolesConfiguration;
        this.userService=userService;
        this.roleService=roleService;
        this.jwtService = jwtService;
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
        RoleDAO roleDAO = roleService.findRoleDAOByRole(RolesConfiguration.ROLE_USER);
        userService.create(new User(registrationRequest.getEmail(),registrationRequest.getPassword(), roleDAO  ));
        UserContext context = UserContext.create(userService.getUser(registrationRequest.getEmail() )) ;
        if (context != null) {
            try {
                String token= jwtService.tokenFor(context);
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

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(){
        return "TEST ANSWER";
    }

}
