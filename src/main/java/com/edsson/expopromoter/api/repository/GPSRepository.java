package com.edsson.expopromoter.api.repository;

import com.edsson.expopromoter.api.model.GpsDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GPSRepository extends CrudRepository<GpsDAO, String> {

}
