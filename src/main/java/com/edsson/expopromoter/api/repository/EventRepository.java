package com.edsson.expopromoter.api.repository;

import com.edsson.expopromoter.api.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {
}
