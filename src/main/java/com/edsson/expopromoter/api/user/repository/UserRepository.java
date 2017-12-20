package com.edsson.expopromoter.api.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.edsson.expopromoter.api.user.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findOneByEmail(String email);

}
