package com.edsson.expopromoter.api.controller;

import com.edsson.expopromoter.api.context.UserContext;
import com.edsson.expopromoter.api.core.service.JwtUtil;
import com.edsson.expopromoter.api.exceptions.*;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.*;
import com.edsson.expopromoter.api.request.DeleteEventRequest;
import com.edsson.expopromoter.api.request.LoginRequest;
import com.edsson.expopromoter.api.service.AdministratorService;
import com.edsson.expopromoter.api.service.EventService;
import com.edsson.expopromoter.api.service.LoginService;
import com.edsson.expopromoter.api.validator.LoginRequestValidator;
import io.swagger.annotations.Api;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/admin")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Api(value = "Admin", description = "Administrator controller")
public class AdministratorController {
    private final AdministratorService service;
    private final LoginRequestValidator loginRequestValidator;
    private final LoginService loginService;
    private final JwtUtil jwtService;
    private final EventService eventService;

    @Autowired
    public AdministratorController(EventService eventService, JwtUtil jwtService, LoginService loginService, AdministratorService service, LoginRequestValidator loginRequestValidator) {
        this.service = service;
        this.jwtService = jwtService;
        this.loginService = loginService;
        this.loginRequestValidator = loginRequestValidator;
        this.eventService = eventService;
    }


    @CrossOrigin
    @RequestMapping(value = "/login", method = POST, produces = APPLICATION_JSON_VALUE,
            consumes = {APPLICATION_JSON_VALUE}
    )
    public JsonAdminUser login(@RequestBody LoginRequest loginRequest,
                               BindingResult bindingResult,
                               HttpServletResponse response,
                               HttpServletRequest request) throws RequestValidationException, FailedToLoginException, PasswordIncorrectException, PermissionsNotEnoughException, NoSuchUserException, InternalServerErrorException {

        String ipAddress = request.getRemoteAddr();
        loginRequestValidator.validate(loginRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
        UserContext userContext = loginService.adminLogin(loginRequest, ipAddress);
        String token = jwtService.tokenFor(userContext);
        double count;
        try {

            count = eventService.pageCount();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
        return JsonAdminUser.from(userContext.getEmail(), token,count);
    }


    @CrossOrigin
    @RequestMapping(
            value = "/delete_event",
            method = POST,
            produces = APPLICATION_JSON_VALUE,
            consumes = {APPLICATION_JSON_VALUE}
    )
    public GenericResponse deleteEvent(@RequestBody DeleteEventRequest deleteEventRequest,
                                 BindingResult bindingResult,
                                 HttpServletResponse response,
                                 HttpServletRequest request) throws RequestValidationException, FailedToLoginException, PrivilegiousException, NoSuchEventPerUserException {


        User user = (User) request.getAttribute("user");
        service.deleteEvent(Integer.parseInt(deleteEventRequest.getId()), user);

        String token = (String) request.getAttribute("Old_Token");
        GenericResponse genericResponse = new GenericResponse(new String[]{});

//        if (!chechToken(request).equals(token)) {
//            genericResponse.setToken((String) request.getAttribute("New_Token"));
//        }
        return genericResponse;
    }


    @CrossOrigin
    @RequestMapping(value = "/get_event_list/{page}", method = GET, produces = APPLICATION_JSON_VALUE,
            consumes = {APPLICATION_JSON_VALUE}
    )
    public JsonEventList getEventList(@PathVariable("page") int page, HttpServletResponse response, HttpServletRequest request) throws InternalServerErrorException {

        JsonEventList jsonEventList = new JsonEventList(service.getEventList(page));

        return jsonEventList;
    }


    @CrossOrigin
    @RequestMapping(value = "/gps_per_event/{id}", method = GET, produces = APPLICATION_JSON_VALUE,
            consumes = {APPLICATION_JSON_VALUE}
    )
    public ListJsonJpsPerEvent gpsPerEvent(@PathVariable("id") Long id, HttpServletResponse response, HttpServletRequest request) throws InternalServerErrorException, GpsForThisEventNotFoundException {
        return service.buildInfoByJps(id);
    }


    private String chechToken(HttpServletRequest request) {

        if (!((String) request.getAttribute("Old_Token")).equals((String) request.getAttribute("New_Token"))) {
            return (String) request.getAttribute("New_Token");
        }
        return (String) request.getAttribute("Old_Token");
    }



@CrossOrigin
    @PostMapping(path = "/upload_csv")

    public void uploadFile(@RequestPart(value = "file") MultipartFile multiPartFile, HttpServletResponse response, HttpServletRequest request) throws IOException, NotFoundException, SystemConfigurationException, ParseException, InternalServerErrorException, FailedToUploadImageToAWSException, EventBadCredentialsException, EntityAlreadyExistException {
        User user = (User) request.getAttribute("user");
        try {
            service.buildEventsFromCsv(multiPartFile, user);
        } catch (Exception e) {
            response.sendError(409);
        }
        response.setStatus(200);
    }
}
