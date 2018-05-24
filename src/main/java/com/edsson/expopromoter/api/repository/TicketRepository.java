package com.edsson.expopromoter.api.repository;


import com.edsson.expopromoter.api.model.TicketDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TicketRepository extends CrudRepository<TicketDAO, String> {
    public TicketDAO findByEventId(int id);
}
