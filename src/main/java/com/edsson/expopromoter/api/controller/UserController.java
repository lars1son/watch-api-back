package com.edsson.expopromoter.api.controller;

import com.edsson.expopromoter.api.context.Messages;
import com.edsson.expopromoter.api.context.UserContext;
import com.edsson.expopromoter.api.exceptions.EntityAlreadyExistException;
import com.edsson.expopromoter.api.exceptions.EventBadCredentialsException;
import com.edsson.expopromoter.api.exceptions.RequestValidationException;
import com.edsson.expopromoter.api.exceptions.SystemConfigurationException;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.GenericResponse;
import com.edsson.expopromoter.api.model.json.JsonTicket;
import com.edsson.expopromoter.api.model.json.JsonUrl;
import com.edsson.expopromoter.api.model.json.JsonUser;
import com.edsson.expopromoter.api.operator.ImageOperator;
import com.edsson.expopromoter.api.request.CreateEventRequest;
import com.edsson.expopromoter.api.request.MergeRequest;
import com.edsson.expopromoter.api.request.UpdatePasswordRequest;
import com.edsson.expopromoter.api.request.UserUpdateRequest;
import com.edsson.expopromoter.api.service.EventService;
import com.edsson.expopromoter.api.service.UserService;
import com.edsson.expopromoter.api.validator.UpdatePasswordRequestValidator;
import javassist.NotFoundException;


import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private static final Logger log = Logger.getLogger(UserController.class);
    private final EventService service;
    private final ImageOperator imageOperator;
    private final UserService userService;
    private final UpdatePasswordRequestValidator updatePasswordRequestValidator;
    @Autowired
    public UserController(EventService service, ImageOperator imageOperator, UpdatePasswordRequestValidator updatePasswordRequestValidator,UserService userService) {
        this.service = service;
        this.updatePasswordRequestValidator=updatePasswordRequestValidator;
        this.imageOperator = imageOperator;
        this.userService = userService;
    }
//
//    // TODO: add logic to include only contacts or related users to requester
//
//    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<User> getUser(@PathVariable(name = "id") String id) {
//        User u = service.findOneById(id);
//        if (u != null) {
//            return new ResponseEntity<>(u, HttpStatus.OK);
//        }
//
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    @SuppressWarnings("unused")
//    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
//    @RolesAllowed(roles = {Role.USER, Role.ADMIN})
//    public ResponseEntity<User> getCurrentUser(@RequestAttribute("user") User requestUser) {
//        User u = service.findOneById(requestUser.getId());
//        if (u == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(u, HttpStatus.OK);
//    }
//
//
//    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
//        User u = new User();
//        String hash = new BCryptPasswordEncoder().encode(request.getPassword());
//        u.setPassword(hash);
//        u.setEmail(request.getEmail());
//
//        Set<Role> roles = new TreeSet<>();
//        roles.add(Role.USER);
//        u.setRoles(roles);
//
//        u.setUserType(UserType.USER);
//
//        User createdUser = service.create(u);
//        if (createdUser != null) {
//            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//        }
//
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }
//
//    @SuppressWarnings("unused")
//    @PostMapping(value = "/createAdmin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @RolesAllowed(roles = {Role.ADMIN})
//    public ResponseEntity<?> createAdmin(@RequestBody CreateUserRequest request) {
//        User u = new User();
//        String hash = new BCryptPasswordEncoder().encode(request.getPassword());
//        u.setPassword(hash);
//        u.setEmail(request.getEmail());
//
//        Set<Role> roles = new TreeSet<>();
//        roles.add(Role.ADMIN);
//        u.setRoles(roles);
//
//        u.setUserType(UserType.ADMIN);
//
//        User createdUser = service.create(u);
//        if (createdUser != null) {
//            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//        }
//
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }
//
//    @SuppressWarnings("unused")
//    @PutMapping(value = "/resetPassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @RolesAllowed(roles = {Role.ADMIN, Role.USER})
//    public ResponseEntity<User> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest, @RequestAttribute("user") User requestUser) {
//        User user = service.findOneById(requestUser.getId());
//
//        if (user == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        String hash = new BCryptPasswordEncoder().encode(resetPasswordRequest.getNewPassword());
//        user.setPassword(hash);
//        service.update(user);
//
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }

    //    @CrossOrigin
//    @RequestMapping(
//            value = "/create_ticket",
//            method = PUT
//            ,
//            produces = APPLICATION_JSON_VALUE,
//            consumes = {ALL_VALUE}
//    )
//    @RolesAllowed(roles = {Role.ADMIN,Role.USER})
//    public JsonUrl createTicket(
//            @RequestBody(required = false)MultipartFile file,
//                                 @RequestBody CreateEventRequest line){
//        System.out.println("====================" );
//        return new JsonUrl(line.getLine());
//    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {APPLICATION_JSON_VALUE, TEXT_PLAIN_VALUE},
            value = "/create_event")
    public JsonUrl createTicket(@RequestBody CreateEventRequest createEventRequest, HttpResponse response,HttpServletRequest request) throws EntityAlreadyExistException, EventBadCredentialsException, ParseException, FileNotFoundException, SystemConfigurationException, IOException, NotFoundException {

        log.info("/Create_event request");
        return service.createEventDAO(createEventRequest, (User) request.getAttribute("user"));

    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {APPLICATION_JSON_VALUE, TEXT_PLAIN_VALUE},
            value = "/update_event")
    public JsonUrl updateEvent(@RequestBody CreateEventRequest createEventRequest,HttpServletRequest request, HttpResponse response) throws ParseException, EventBadCredentialsException, NotFoundException, IOException, SystemConfigurationException {
//        response.setHeader("Token", (String) request.getAttribute("Token"));
        return service.update(createEventRequest,request);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/show_all_tickets")
    public List<JsonTicket> getAllTicketsPerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        response.setHeader("Token", (String) request.getAttribute("Token"));
        return userService.getAllTickets(request);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/update_user_info")
    public GenericResponse updateUser(@RequestBody UserUpdateRequest userUpdateRequest,HttpServletRequest request, HttpResponse response) throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        userService.update(userUpdateRequest, (User) request.getAttribute("user"));
        return new GenericResponse("User update success", new String[]{});
    }


//
//    @RequestMapping(
//            value = "/update_password",
//            method = POST,
//            produces = APPLICATION_JSON_VALUE,
//            consumes = {APPLICATION_JSON_VALUE, TEXT_PLAIN_VALUE}
//    )
//    public GenericResponse updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest,HttpServletRequest request,
//                               BindingResult bindingResult) throws Exception {
//        updatePasswordRequestValidator.validate(updatePasswordRequest, bindingResult);
//        if (bindingResult.hasErrors()) {
//            throw new RequestValidationException(bindingResult);
//        }
//
//
//        User user = (User) request.getAttribute("user");
//        userService.update(user.getEmail(),updatePasswordRequest.getNewPassword(),user);
//        return new GenericResponse("Password update success", new String[]{});
//    }


}
