package com.edsson.expopromoter.api.repository;


import com.edsson.expopromoter.api.model.EventDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<EventDAO, String> {
    EventDAO findByName(String name);

    EventDAO findById(Integer id);

    List<EventDAO> findByIdAndUpdatedAtAfter(Long id , Date date);
}
