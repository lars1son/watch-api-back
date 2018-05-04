package com.edsson.expopromoter.api.repository;

import com.edsson.expopromoter.api.model.RoleDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleDAO,String> {

    RoleDAO findRoleDAOByRole(String s);
}
