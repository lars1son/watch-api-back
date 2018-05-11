package com.edsson.expopromoter.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.edsson.expopromoter.api.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findOneByEmail(String email);
    User findOneById(Long id);
}
