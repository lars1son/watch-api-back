package com.edsson.expopromoter.api.operator;

import com.edsson.expopromoter.api.config.SystemConfigurationKeys;
import com.edsson.expopromoter.api.exceptions.SystemConfigurationException;
import com.edsson.expopromoter.api.service.system_configuration.SystemConfigurationServiceImpl;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.ejb.Singleton;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
@Singleton
public class ImageOperator {
    private final SystemConfigurationServiceImpl systemConfigurationService;

    public ImageOperator(SystemConfigurationServiceImpl systemConfigurationService) {
        this.systemConfigurationService = systemConfigurationService;
    }

    public void saveImage(String imageBase64) throws IOException,SystemConfigurationException {
        String crntImage = imageBase64;
        byte[] data = Base64.decodeBase64(crntImage);
        try (OutputStream stream = new FileOutputStream(systemConfigurationService.getValueByKey(SystemConfigurationKeys.DefaultImagePath.PATH) + FileInfoService.findFileExtension(crntImage))) {
            stream.write(data);
        }

    }
}
