package com.edsson.expopromoter.api.exceptions;

import static java.lang.String.format;

public class NoSuchSystemConfigurationKey extends SystemConfigurationException {
    public NoSuchSystemConfigurationKey(String key) {
        super(format("No such system configuration key: %s", key));
    }
}
