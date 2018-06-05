package com.edsson.expopromoter.api.config;

import java.util.Arrays;

public class SystemConfigurationKeys {

    public static final class DefaultImagePath {
        public final static String PATH = "default_event_image_path";
    }

    public static final class EventInfoURL{
        public final static String URL = "default_event_url";
    }


    public static final class DefaultUserTicketImagePath{
        public final static String PATH = "default_ticket_loaded_image_path";
    }



    public static final class Password{
        public final static String RESET_LINK_TIMEOUT = "password_reset_link_timeout";
    }
    public static final String[] SYSTEM_CONFIGURATION_KEYS = {
            DefaultImagePath.PATH
            , EventInfoURL.URL
            , DefaultUserTicketImagePath.PATH
            , Password.RESET_LINK_TIMEOUT
    };


    public static String[] getSystemConfigurationKeys(){
        return SYSTEM_CONFIGURATION_KEYS;
    }

    /**
     * Check if key exist in system configuration.
     * @param key
     * @return boolean
     */
    public static boolean isSystemConfigurationKeyExist(String key){
        return Arrays.asList(SYSTEM_CONFIGURATION_KEYS).contains(key);
    }
}
