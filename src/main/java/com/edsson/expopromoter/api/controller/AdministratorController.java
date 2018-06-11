package com.edsson.expopromoter.api.controller;

import com.edsson.expopromoter.api.context.Messages;
import com.edsson.expopromoter.api.exceptions.FailedToLoginException;
import com.edsson.expopromoter.api.exceptions.NoSuchEventPerUserException;
import com.edsson.expopromoter.api.exceptions.PrivilegiousException;
import com.edsson.expopromoter.api.exceptions.RequestValidationException;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.GenericResponse;
import com.edsson.expopromoter.api.request.DeleteEventRequest;
import com.edsson.expopromoter.api.service.AdministratorService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/admin")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Api(value = "Admin", description = "Administrator controller")
public class AdministratorController {
    private final AdministratorService service;

    @Autowired
    public AdministratorController(AdministratorService service) {
        this.service = service;
    }

    @CrossOrigin
    @RequestMapping(
            value = "/delete_event",
            method = POST,
            produces = APPLICATION_JSON_VALUE,
            consumes = {APPLICATION_JSON_VALUE}
    )
    public GenericResponse login(@RequestBody DeleteEventRequest deleteEventRequest,
                                 BindingResult bindingResult,
                                 HttpServletResponse response,
                                 HttpServletRequest request) throws RequestValidationException, FailedToLoginException, PrivilegiousException, NoSuchEventPerUserException {

        User user = (User) request.getAttribute("user");
        service.deleteEvent(Integer.parseInt(deleteEventRequest.getId()), user);

        return new GenericResponse(Messages.MESSAGE_EVENT_DELETE_BY_ADMINISTRATOR, new String[]{});
    }


}
