package com.edsson.expopromoter.api.repository;


import com.edsson.expopromoter.api.model.EventDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<EventDAO, String> {
    EventDAO findByName(String name);

    EventDAO findById(Integer id);

}
