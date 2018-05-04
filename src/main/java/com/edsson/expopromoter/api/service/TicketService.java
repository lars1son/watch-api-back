package com.edsson.expopromoter.api.service;

import com.edsson.expopromoter.api.model.Ticket;
import com.edsson.expopromoter.api.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final TicketRepository repository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.repository = ticketRepository;
    }

    public Ticket findOneById(String id) {
        return repository.findOne(id);
    }
}
