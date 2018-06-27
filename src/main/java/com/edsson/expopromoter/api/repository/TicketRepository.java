package com.edsson.expopromoter.api.repository;


import com.edsson.expopromoter.api.model.TicketDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface TicketRepository extends CrudRepository<TicketDAO, String> {


    TicketDAO findOneById(int id);

    @Modifying
    @Transactional
    @Query(value="delete from TicketDAO t where t.id = ?1")
    void removeById(Integer id);
}
