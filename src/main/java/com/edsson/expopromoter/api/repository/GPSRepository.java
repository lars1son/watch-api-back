package com.edsson.expopromoter.api.repository;

import com.edsson.expopromoter.api.model.GpsDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GPSRepository extends CrudRepository<GpsDAO, String> {

    List<GpsDAO> findAllByEventDAO_Id(Integer id);
}
