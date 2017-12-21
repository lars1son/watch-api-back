package com.edsson.expopromoter.api.ticket.controller;

import com.edsson.expopromoter.api.ticket.model.Ticket;
import com.edsson.expopromoter.api.ticket.service.TicketService;
import com.edsson.expopromoter.api.user.model.User;
import com.edsson.expopromoter.api.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/tickets")
public class TicketController {

    private final TicketService ticketService;

    private final UserService userService;

    @Autowired
    public TicketController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    // TODO: finish

    @GetMapping(value = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Ticket>> getUser(@RequestAttribute("user") User requestUser) {
        User u = userService.findOneById(requestUser.getId());

        if (u == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return null;
    }
}
