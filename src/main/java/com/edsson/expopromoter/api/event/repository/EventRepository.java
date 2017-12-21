package com.edsson.expopromoter.api.event.repository;

import com.edsson.expopromoter.api.event.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {
}
