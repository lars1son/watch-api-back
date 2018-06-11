package com.edsson.expopromoter.api.controller;


import com.edsson.expopromoter.api.exceptions.EntityAlreadyExistException;
import com.edsson.expopromoter.api.exceptions.FailedToUploadImageToAWSException;
import com.edsson.expopromoter.api.exceptions.SystemConfigurationException;
import com.edsson.expopromoter.api.model.json.JsonTicket;
import com.edsson.expopromoter.api.request.AddTicketRequest;
import com.edsson.expopromoter.api.service.TicketService;
import javax.servlet.http.HttpServletRequest;import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/tickets")
public class TicketController {

    private final TicketService ticketService;


    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;

    }

    //    @GetMapping(value = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<Ticket>> getUser(@RequestAttribute("user") User requestUser) {
//        User u = userService.findOneById(requestUser.getId());
//
//        if (u == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        List<Ticket> tickets = u.getTickets();
//
//        return new ResponseEntity<List<Ticket>>(tickets, HttpStatus.OK);
//    }
    @RequestMapping(method = RequestMethod.POST, path = "/add")
    public JsonTicket addTicket(@RequestBody AddTicketRequest addTicketRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, SystemConfigurationException, EntityAlreadyExistException, FailedToUploadImageToAWSException {
        return new JsonTicket(ticketService.addUserTicketFoEvent(addTicketRequest, request));
    }


}
