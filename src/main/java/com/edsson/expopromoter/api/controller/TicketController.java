package com.edsson.expopromoter.api.controller;


import com.edsson.expopromoter.api.context.Messages;
import com.edsson.expopromoter.api.exceptions.*;
import com.edsson.expopromoter.api.model.json.GenericResponse;
import com.edsson.expopromoter.api.model.json.JsonTicket;
import com.edsson.expopromoter.api.request.AddTicketRequest;
import com.edsson.expopromoter.api.request.DeleteTicketRequest;
import com.edsson.expopromoter.api.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/tickets")
public class TicketController {

    private final TicketService ticketService;


    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;

    }


    @RequestMapping(method = RequestMethod.POST, path = "/add")
    public JsonTicket addTicket(@RequestBody AddTicketRequest addTicketRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, SystemConfigurationException, EntityAlreadyExistException, FailedToUploadImageToAWSException, EntityNotFoundException {
        return new JsonTicket(ticketService.addUserTicketFoEvent(addTicketRequest, request));
    }


    @RequestMapping(method = RequestMethod.POST, path = "/delete")
    public GenericResponse deleteTicket(@RequestBody DeleteTicketRequest deleteTicketRequest, HttpServletRequest request) throws NoSuchTicketPerUserException {
        ticketService.deleteTicket(deleteTicketRequest.getId(), request);
//        return new GenericResponse(Messages.MESSAGE_DELETE_TICKET_SUCCESS,new String[]{});
        return new GenericResponse( new String[]{});
    }

}
