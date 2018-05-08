package com.edsson.expopromoter.api.service.system_configuration;


import com.edsson.expopromoter.api.config.SystemConfigurationKeys;
import com.edsson.expopromoter.api.context.UserContext;
import com.edsson.expopromoter.api.exceptions.NoSuchSystemConfigurationKey;
import com.edsson.expopromoter.api.exceptions.SystemConfigurationException;
import com.edsson.expopromoter.api.model.SystemConfigurationDAO;
import com.edsson.expopromoter.api.repository.SystemConfigurationRepository;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class SystemConfigurationServiceImpl implements SystemConfigurationService {

    private HashMap<String, SystemConfigurationDAO> systemConfigurationCache = new HashMap<>();

//    private static final Logger LOG = LoggerFactory.getLogger(SystemConfigurationServiceImpl.class);

    private Environment env;


    @Autowired
    private SystemConfigurationRepository repository;



    @Autowired
    public SystemConfigurationServiceImpl(Environment env) {
        this.env = env;
    }

    @Override
    @Transactional
    public String getValueByKey(final String key) throws SystemConfigurationException {

        // Return cached value if exists
        String cachedSystemConfigValue = getValueByKeyFromCache(key);


        // Otherwise get it from database
        SystemConfigurationDAO systemConfigurationDAO = repository.findOne(key);

        if (systemConfigurationDAO != null) {
        }

        if (systemConfigurationDAO == null) {

            String value = env.getProperty(key);
            if (value != null) {
//                LOG.warn("can not get property[" + value + "] from DB. return from application.properties");
                return value;

            } else {
                throw new NoSuchSystemConfigurationKey(key);
            }

        }

        // Put to cache configuration from database
        updateCachedValue(systemConfigurationDAO);

        return systemConfigurationDAO.getValue();
    }

    @Override
    public String getAsString(String key, String defaultValue) {

        try {

            String configValue = getValueByKey(key);

            return (configValue == null || configValue.equals("0")) ? defaultValue : configValue;

        } catch (SystemConfigurationException e) {
//            LOG.warn("getAsString " + e.getMessage());
            return defaultValue;
        }

    }

    @Override
    public Integer getAsInteger(String key, Integer defaultValue) {
        try {
            String configValue = getValueByKey(key);
            return (configValue == null || configValue.equals("0")) ? defaultValue : Integer.parseInt(configValue);
        } catch (SystemConfigurationException | NumberFormatException e) {
//            LOG.warn("getAsInteger " + e.getMessage());
            return defaultValue;
        }

    }
//
//    @Override
//    public SystemConfigurationDAO saveSystemConfiguration(final String key, final String value, UserDAO changingUser)
//            throws NoSuchSystemConfigurationKey {
//
//        SystemConfigurationDAO systemConfigurationDAO = new SystemConfigurationDAO();
//        systemConfigurationDAO.setKey(key);
//        systemConfigurationDAO.setValue(value);
//
//        return saveSystemConfiguration(systemConfigurationDAO, changingUser);
//    }
//
//    private SystemConfigurationDAO saveSystemConfiguration(final SystemConfigurationDAO systemConfigurationDAO, UserDAO changingUser)
//            throws NoSuchSystemConfigurationKey {
//
//        // Get config from database
//        SystemConfigurationDAO oldRepositoryConfig = repository.findOne(systemConfigurationDAO.getKey());
//        SystemConfigurationDAO oldSystemConfiguration = new SystemConfigurationDAO();
//
//        if (oldRepositoryConfig != null) {
//
//            // -- New object to prevent mutability
//
//            oldSystemConfiguration.setKey(oldRepositoryConfig.getKey());
//            oldSystemConfiguration.setValue(oldRepositoryConfig.getValue());
//            oldSystemConfiguration.setCreated(oldRepositoryConfig.getCreated());
//            oldSystemConfiguration.setUpdated(oldRepositoryConfig.getUpdated());
//        }
//
//        final SystemConfigurationDAO savedSystemConfiguration = repository.saveAndFlush(systemConfigurationDAO);
//
//        // Put to cache configuration from database
//        updateCachedValue(savedSystemConfiguration);
//
//        // Save history
//        historyRepository.save(createFromSystemConfigurations(
//                oldSystemConfiguration,
//                systemConfigurationDAO,
//                changingUser
//        ));
//
//        return savedSystemConfiguration;
//    }
//
    @Override
    public List<SystemConfigurationDAO> findAll() {
        return repository.findAll();
    }

    private String getValueByKeyFromCache(String key) throws NoSuchSystemConfigurationKey {

        if (!SystemConfigurationKeys.isSystemConfigurationKeyExist(key)) {
            throw new NoSuchSystemConfigurationKey(key);
        }

        SystemConfigurationDAO cachedConfig = systemConfigurationCache.get(key);

        return cachedConfig == null ? null : cachedConfig.getValue();
    }

    private void updateCachedValue(final SystemConfigurationDAO systemConfiguration)
            throws NoSuchSystemConfigurationKey {
        systemConfigurationCache.put(systemConfiguration.getKey(), systemConfiguration);
    }
//
//    private SystemConfigurationHistoryDAO createFromSystemConfigurations(SystemConfigurationDAO oldConfig
//            , SystemConfigurationDAO config, UserDAO changingUser) {
//
//        SystemConfigurationHistoryDAO history = new SystemConfigurationHistoryDAO();
//
//        history.setKey(config.getKey());
//        history.setValue(config.getValue());
//        history.setPreviousValue(oldConfig == null ? null : oldConfig.getValue());
//        history.setUser(changingUser);
//
//        return history;
//    }
//
//    /**
//     * Get configurations from database and update cache if it's different from database value.
//     *
//     * @throws SystemConfigurationException
//     */
//    public void updateSystemConfigurationCache() throws SystemConfigurationException {
//
//        List<SystemConfigurationDAO> systemConfigurations = repository.findAll();
//
//        if (systemConfigurations == null) {
//            throw new SystemConfigurationException("There is no system configs in database.");
//        }
//
//        for (SystemConfigurationDAO config : systemConfigurations) {
//
//            String cachedValue = getValueByKeyFromCache(config.getKey());
//
//            // Ignore if config same as in cache
//            if (cachedValue != null && cachedValue.equals(config.getValue())) {
//                continue;
//            }
//
//            // Otherwise update cache
//            updateCachedValue(config);
//        }
//
//    }

    public void saveDefaultConfiguration(UserContext userContext) {


        for (String key : SystemConfigurationKeys.SYSTEM_CONFIGURATION_KEYS) {

//            String value = env.getProperty(key);
//
//            if (value == null) {
//                LOG.error("System Configuration property empty: " + key);
//                continue;
//            }

            // Ignore saving defaults if key exist in database
            SystemConfigurationDAO savedConfig = repository.findOne(key);
            if (savedConfig != null && savedConfig.getValue() != null) {
//                LOG.warn("System Configuration already exist in database: " + key);
                continue;
            }

            try {

                SystemConfigurationDAO systemConfigurationDAO = new SystemConfigurationDAO();
                systemConfigurationDAO.setKey(key);
//                systemConfigurationDAO.setValue(value);
//                saveSystemConfiguration(systemConfigurationDAO, userContext.toUserDAO());

            } catch (NoSuchSystemConfigurationKey e) {
//                LOG.error("SaveDefaultConfiguration error: " + e.getMessage());
                continue;
            }

        }
    }


}
