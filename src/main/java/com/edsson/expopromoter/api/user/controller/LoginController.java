package com.edsson.expopromoter.api.user.controller;

import com.edsson.expopromoter.api.core.service.JwtUtil;
import com.edsson.expopromoter.api.user.model.Credential;
import com.edsson.expopromoter.api.user.model.User;
import com.edsson.expopromoter.api.user.request.LoginRequest;
import com.edsson.expopromoter.api.user.request.ResetPasswordRequest;
import com.edsson.expopromoter.api.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/login")
@Api(value = "Description of value", description = "This API provides the capability to login user", produces = "application/json")
public class LoginController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public LoginController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @SuppressWarnings("unused")
    @ApiOperation(value = "Make login method", produces = "application/json")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> login(@Valid @RequestBody LoginRequest loginRequest) {
        //Try to login as user
        User u = userService.login(loginRequest);

        if ( u == null ) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        HttpHeaders headers = new HttpHeaders();

        Map<String, Object> claims = new HashMap<>();
        claims.put("user", new Credential(u));

        List<String> auth = new ArrayList<>(1);
        auth.add(jwtUtil.generateToken(claims));
        headers.put("Authorization", auth);

        return new ResponseEntity<>(u, headers, HttpStatus.OK);
    }
}
