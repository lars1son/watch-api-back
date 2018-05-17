package com.edsson.expopromoter.api.operator;

import com.edsson.expopromoter.api.config.SystemConfigurationKeys;
import com.edsson.expopromoter.api.exceptions.SystemConfigurationException;
import com.edsson.expopromoter.api.service.system_configuration.SystemConfigurationServiceImpl;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.ejb.Singleton;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Service
@Singleton
public class ImageOperator {
    private final SystemConfigurationServiceImpl systemConfigurationService;

    public ImageOperator(SystemConfigurationServiceImpl systemConfigurationService) {
        this.systemConfigurationService = systemConfigurationService;
    }

    public String saveImage(String imageBase64, String path, String fileName) throws IOException, SystemConfigurationException {
//        String crntImage = imageBase64;
//         path = systemConfigurationService.getValueByKey(SystemConfigurationKeys.DefaultImagePath.PATH) + "\\" + id;
        File files = new File(path);
        if (!files.exists()) {
            if (files.mkdirs()) {
                System.out.println("Multiple directories are created!");
            } else {
                System.out.println("Failed to create multiple directories!");
            }
        }


        path = path + "\\" + fileName + "_" + FileInfoService.findFileExtension(imageBase64);

        byte[] data = Base64.decodeBase64(imageBase64);


        try (OutputStream stream = new FileOutputStream(path)) {
            stream.write(data);
            return (path);
        }


    }

    public String encodeFileToBase64Binary(String filename) throws IOException {
        File file = new File(filename);
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        return new String(encoded, StandardCharsets.US_ASCII);
    }
}
