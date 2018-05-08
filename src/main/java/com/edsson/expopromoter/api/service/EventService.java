package com.edsson.expopromoter.api.service;


import com.edsson.expopromoter.api.model.EventDAO;
import com.edsson.expopromoter.api.model.TicketDAO;
import com.edsson.expopromoter.api.repository.EventRepository;
import com.edsson.expopromoter.api.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository repository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.repository = eventRepository;
    }

    public EventDAO findOneById(String id) {
        return repository.findOne(id);
    }

    public void create(EventDAO eventDAO){
        repository.save(eventDAO);
    }
}
