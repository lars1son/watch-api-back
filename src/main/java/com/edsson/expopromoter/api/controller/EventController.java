package com.edsson.expopromoter.api.controller;

import com.edsson.expopromoter.api.context.Messages;
import com.edsson.expopromoter.api.exceptions.InternalServerErrorException;
import com.edsson.expopromoter.api.exceptions.NoSuchEventPerUserException;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.GenericResponse;
import com.edsson.expopromoter.api.model.json.JsonEventInfo;
import com.edsson.expopromoter.api.operator.ImageOperator;
import com.edsson.expopromoter.api.request.AddGPSRequest;
import com.edsson.expopromoter.api.request.DeleteEventRequest;
import com.edsson.expopromoter.api.request.GetUpdatedEventsRequest;
import com.edsson.expopromoter.api.service.EventService;
import com.edsson.expopromoter.api.service.UserService;
import io.swagger.annotations.Api;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/event")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Api(value = "event", description = "Event controller")
public class EventController {
    final static Logger logger = Logger.getLogger(EventController.class);
    private final EventService service;
    private final ImageOperator imageOperator;
    private final UserService userService;

    @Autowired
    public EventController(UserService userService, EventService eventService, ImageOperator imageOperator) {
        this.service = eventService;
        this.imageOperator = imageOperator;
        this.userService = userService;
    }


    @RequestMapping(method = RequestMethod.GET,
            value = "/{id}")
    public JsonEventInfo getEventInfo(@PathVariable("id") int id, HttpServletRequest request, HttpResponse response) throws IOException {
//        response.setHeader("Token", (String) request.getAttribute("Token"));
        return service.buildWithImage(service.findOneById(id));
    }


    @RequestMapping(method = RequestMethod.POST, value = "/update")
    public List<JsonEventInfo> getUpdatedEvents(@RequestBody GetUpdatedEventsRequest updatedEventsRequest, HttpServletRequest request) throws ParseException, InternalServerErrorException {
        try {
            return service.getUpdatedEvent(updatedEventsRequest, ((User) request.getAttribute("user")).getId());
        }
        catch (Exception e){
            logger.error("Event update error. ",e);
            throw new InternalServerErrorException();
        }
    }


    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    public GenericResponse getUpdatedEvents(@RequestBody DeleteEventRequest deleteEventRequest, HttpServletRequest request) throws ParseException, NoSuchEventPerUserException {
        User user = (User) request.getAttribute("user");

        try {
            service.deleteEvent(Integer.valueOf(deleteEventRequest.getId()), user);
        } catch (NoSuchEventPerUserException e) {
            logger.error("Delete event failed", e);
            return new GenericResponse(Messages.MESSAGE_DELETE_EVENT_FAILED, new String[]{});
        }

        return new GenericResponse( new String[]{});
//        return new GenericResponse(Messages.MESSAGE_DELETE_EVENT_SUCCESS, new String[]{});
    }


    @RequestMapping(method = RequestMethod.POST, value = "/add_gps")
    public GenericResponse addGps(@RequestBody AddGPSRequest gpsRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        service.createGPS(gpsRequest, user);
        return new GenericResponse( new String[]{});
//        return new GenericResponse(Messages.MESSAGE_ADD_GPS_SUCCESS, new String[]{});
    }


}
