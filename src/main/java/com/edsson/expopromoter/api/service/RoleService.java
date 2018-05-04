package com.edsson.expopromoter.api.service;

import com.edsson.expopromoter.api.model.RoleDAO;
import com.edsson.expopromoter.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService{
    private final RoleRepository repository;
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.repository=roleRepository;
    }

    public RoleDAO findRoleDAOByRole(String role) {
        return repository.findRoleDAOByRole(role);
    }
}
