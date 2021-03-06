package com.edsson.expopromoter.api.repository;


import com.edsson.expopromoter.api.model.EventDAO;
import com.edsson.expopromoter.api.model.User;
import org.springframework.data.domain.Pageable;
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
//    @Query(value="select from EventDAO e where e.name = ?1  and e.userCreatorId= ?2")
   List<EventDAO>  findListByName(String name );
    @Modifying
    @Transactional
    @Query(value="delete from EventDAO e where e.id = ?1")
    void removeEventDAOById(Integer id);
    List<EventDAO> findByIdAndUpdatedAtAfter(Long id , Date date);


    @Query(value = "select e from EventDAO e")
    List<EventDAO> getEventsById(Pageable pageable);

    @Query(value="select e from EventDAO e where e.id <= ?1  and e.id > ?2")
    List<EventDAO> getListOfEventInInterval(Integer from, Integer to);

    @Query(value = "select max(e.id) from EventDAO e")
    Long getMaxId();

}
