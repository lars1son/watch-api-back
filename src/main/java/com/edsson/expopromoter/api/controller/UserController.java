package com.edsson.expopromoter.api.controller;

import com.edsson.expopromoter.api.exceptions.EventBadCredentialsException;
import com.edsson.expopromoter.api.exceptions.SystemConfigurationException;
import com.edsson.expopromoter.api.model.EventDAO;
import com.edsson.expopromoter.api.operator.ImageOperator;
import com.edsson.expopromoter.api.request.CreateEventRequest;
import com.edsson.expopromoter.api.service.EventService;
import com.edsson.expopromoter.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final EventService service;
    private final ImageOperator imageOperator;

    @Autowired
    public UserController(EventService service, ImageOperator imageOperator) {
        this.service = service;
        this.imageOperator = imageOperator;
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
//    public JsonTest createTicket(
//            @RequestBody(required = false)MultipartFile file,
//                                 @RequestBody CreateEventRequest line){
//        System.out.println("====================" );
//        return new JsonTest(line.getLine());
//    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {APPLICATION_JSON_VALUE, TEXT_PLAIN_VALUE},
            value = "/create_event")
    public void createTicket(@RequestBody CreateEventRequest createEventRequest) throws EventBadCredentialsException, ParseException, FileNotFoundException, SystemConfigurationException, IOException {
        EventDAO eventDAO = new EventDAO();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

        if (createEventRequest.getName() != null && createEventRequest.getDateStart() != null  && createEventRequest.getDateEnd()!=null) {
            eventDAO.setName(createEventRequest.getName());
            eventDAO.setDateStart(formatter.parse(createEventRequest.getDateStart()));
            eventDAO.setDateEnd(formatter.parse(createEventRequest.getDateEnd()));

        }
        else throw new EventBadCredentialsException("Bad Credentials");

        eventDAO.setAgenda(createEventRequest.getAgenda());
        eventDAO.setContacts(createEventRequest.getContacts());
        eventDAO.setDescription(createEventRequest.getDescription());
        eventDAO.setEventLocation(createEventRequest.getLocation());
        eventDAO.setEventWebsite(createEventRequest.getWebsite());

        service.create(eventDAO);

    }
}
