//package com.edsson.expopromoter.api.controller;
//
//import com.edsson.expopromoter.api.model.Ticket;
//import com.edsson.expopromoter.api.service.TicketService;
//import com.edsson.expopromoter.api.model.User;
//import com.edsson.expopromoter.api.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@SuppressWarnings("unused")
//@RestController
//@RequestMapping(value = "/tickets")
//public class TicketController {
//
//    private final TicketService ticketService;
//
//    private final UserService userService;
//
//    @Autowired
//    public TicketController(TicketService ticketService, UserService userService) {
//        this.ticketService = ticketService;
//        this.userService = userService;
//    }
//
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
//}
