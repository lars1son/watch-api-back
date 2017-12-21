package com.edsson.expopromoter.api.user.controller;

import com.edsson.expopromoter.api.core.filter.RolesAllowed;
import com.edsson.expopromoter.api.user.model.Role;
import com.edsson.expopromoter.api.user.model.User;
import com.edsson.expopromoter.api.user.model.UserType;
import com.edsson.expopromoter.api.user.request.CreateUserRequest;
import com.edsson.expopromoter.api.user.request.ResetPasswordRequest;
import com.edsson.expopromoter.api.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    // TODO: add logic to include only contacts or related users to requester

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable(name = "id") String id) {
        User u = service.findOneById(id);
        if (u != null) {
            return new ResponseEntity<>(u, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @SuppressWarnings("unused")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(roles = {Role.USER, Role.ADMIN})
    public ResponseEntity<User> getCurrentUser(@RequestAttribute("user") User requestUser) {
        User u = service.findOneById(requestUser.getId());
        if (u == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @SuppressWarnings("unused")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        User u = new User();
        String hash = new BCryptPasswordEncoder().encode(request.getPassword());
        u.setPassword(hash);
        u.setEmail(request.getEmail());

        Set<Role> roles = new TreeSet<>();
        roles.add(Role.USER);
        u.setRoles(roles);

        u.setUserType(UserType.USER);

        User createdUser = service.create(u);
        if (createdUser != null) {
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @SuppressWarnings("unused")
    @PostMapping(value = "/createAdmin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(roles = {Role.ADMIN})
    public ResponseEntity<?> createAdmin(@RequestBody CreateUserRequest request) {
        User u = new User();
        String hash = new BCryptPasswordEncoder().encode(request.getPassword());
        u.setPassword(hash);
        u.setEmail(request.getEmail());

        Set<Role> roles = new TreeSet<>();
        roles.add(Role.ADMIN);
        u.setRoles(roles);

        u.setUserType(UserType.ADMIN);

        User createdUser = service.create(u);
        if (createdUser != null) {
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @SuppressWarnings("unused")
    @PutMapping(value = "/resetPassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(roles = {Role.ADMIN, Role.USER})
    public ResponseEntity<User> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest, @RequestAttribute("user") User requestUser) {
        User user = service.findOneById(requestUser.getId());

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String hash = new BCryptPasswordEncoder().encode(resetPasswordRequest.getNewPassword());
        user.setPassword(hash);
        service.update(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
