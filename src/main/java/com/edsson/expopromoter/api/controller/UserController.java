package com.edsson.expopromoter.api.controller;

import com.edsson.expopromoter.api.core.service.JwtUtil;
import com.edsson.expopromoter.api.exceptions.*;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.GenericResponse;
import com.edsson.expopromoter.api.model.json.JsonTicket;
import com.edsson.expopromoter.api.model.json.JsonToken;
import com.edsson.expopromoter.api.model.json.JsonUrl;
import com.edsson.expopromoter.api.operator.ImageOperator;
import com.edsson.expopromoter.api.request.CreateEventRequest;
import com.edsson.expopromoter.api.request.UserUpdateRequest;
import com.edsson.expopromoter.api.service.EventService;
import com.edsson.expopromoter.api.service.TokenService;
import com.edsson.expopromoter.api.service.UserService;
import com.edsson.expopromoter.api.validator.UpdatePasswordRequestValidator;
import javassist.NotFoundException;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Set;

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
    public UserController(  EventService service, ImageOperator imageOperator, UpdatePasswordRequestValidator updatePasswordRequestValidator, UserService userService) {
        this.service = service;
        this.updatePasswordRequestValidator = updatePasswordRequestValidator;
        this.imageOperator = imageOperator;

        this.userService = userService;

    }





    @RequestMapping(method = RequestMethod.POST,
            consumes = {APPLICATION_JSON_VALUE, TEXT_PLAIN_VALUE},
            value = "/create_event")
    public JsonUrl createTicket(@RequestBody CreateEventRequest createEventRequest, HttpResponse response, HttpServletRequest request) throws EntityAlreadyExistException, EventBadCredentialsException, ParseException, FileNotFoundException, SystemConfigurationException, IOException, NotFoundException, FailedToUploadImageToAWSException, InternalServerErrorException {

        log.info("Create event request");
        try {
            return service.createEventDAO(createEventRequest, (User) request.getAttribute("user"));
        }
        catch (Exception e){
            e.printStackTrace();
            throw new EventBadCredentialsException();
        }
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {APPLICATION_JSON_VALUE, TEXT_PLAIN_VALUE},
            value = "/update_event")
    public JsonUrl updateEvent(@RequestBody CreateEventRequest createEventRequest, HttpServletRequest request, HttpResponse response) throws ParseException, EventBadCredentialsException, NotFoundException, IOException, SystemConfigurationException, FailedToUploadImageToAWSException {
//        response.setHeader("Token", (String) request.getAttribute("Token"));
        return service.update(createEventRequest, request);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/show_all_tickets")
    public Set<JsonTicket> getAllTicketsPerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        response.setHeader("Token", (String) request.getAttribute("Token"));
        return userService.getAllTickets(request);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/update_user_info")
    public GenericResponse updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request, HttpResponse response) throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        userService.update(userUpdateRequest, (User) request.getAttribute("user"));
//        return new GenericResponse("User update success", new String[]{});
        return new GenericResponse(new String[]{});
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
