package com.edsson.expopromoter.api.service;


import com.edsson.expopromoter.api.model.TicketDAO;
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

    public TicketDAO findOneById(String id) {
        return repository.findOne(id);
    }
}
