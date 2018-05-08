package com.edsson.expopromoter.api.service.system_configuration;


import com.edsson.expopromoter.api.exceptions.SystemConfigurationException;
import com.edsson.expopromoter.api.model.SystemConfigurationDAO;

import java.util.List;

public interface SystemConfigurationService {

    String getValueByKey(final String key) throws SystemConfigurationException;

    String getAsString(final String key, final String defaultValue);

    Integer getAsInteger(final String key, final Integer defaultValue);

//    SystemConfigurationDAO saveSystemConfiguration(final String key, final String value, UserDAO changingUser) throws SystemConfigurationException;

    List<SystemConfigurationDAO> findAll();

}
