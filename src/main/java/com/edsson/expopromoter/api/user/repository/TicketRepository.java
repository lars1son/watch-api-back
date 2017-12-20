package com.edsson.expopromoter.api.user.repository;

import com.edsson.expopromoter.api.ticket.model.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, String> {
}
