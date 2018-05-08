package com.edsson.expopromoter.api.repository;


import com.edsson.expopromoter.api.model.SystemConfigurationDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemConfigurationRepository
        extends JpaRepository<SystemConfigurationDAO, String> {
}
