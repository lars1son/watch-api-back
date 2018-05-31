package com.edsson.expopromoter.api.repository;


import com.edsson.expopromoter.api.model.EventDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<EventDAO, String> {
    EventDAO findByName(String name);

    EventDAO findById(Integer id);

    @Modifying
    @Transactional
    @Query(value="delete from EventDAO e where e.id = ?1")
    void removeEventDAOById(Integer id);
    List<EventDAO> findByIdAndUpdatedAtAfter(Long id , Date date);
}
