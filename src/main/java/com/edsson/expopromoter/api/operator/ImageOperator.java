package com.edsson.expopromoter.api.operator;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ListTablesRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.edsson.expopromoter.api.exceptions.FailedToUploadImageToAWSException;
import com.edsson.expopromoter.api.service.system_configuration.SystemConfigurationServiceImpl;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ejb.Singleton;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@Singleton
public class ImageOperator {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ImageOperator.class);

    private final SystemConfigurationServiceImpl systemConfigurationService;

    @Value("${amazon.bucketName}")
    private String bucketName;
    @Value("${amazon.accessKey}")
    private String accessKey;
    @Value("${amazon.secretKey}")
    private String secretKey;

    public ImageOperator(SystemConfigurationServiceImpl systemConfigurationService) {
        this.systemConfigurationService = systemConfigurationService;
    }

//    public String saveImage(String imageBase64, String path) throws IOException, SystemConfigurationException {
////        String crntImage = imageBase64;
////         path = systemConfigurationService.getValueByKey(SystemConfigurationKeys.DefaultImagePath.PATH) + "\\" + id;
//        File files = new File(path);
////        if (!files.exists()) {
////            if (files.mkdirs()) {
////                System.out.println("Multiple directories are created!");
////            } else {
////                System.out.println("Failed to create multiple directories!");
////            }
////        }
//
//
////        path = path + "\\" + fileName + "_" + FileInfoService.findFileExtension(imageBase64);
//
//        path = path + FileInfoService.findFileExtension(imageBase64);
//
//        byte[] data = Base64.decodeBase64(imageBase64);
//
//
//        try (OutputStream stream = new FileOutputStream(path)) {
//            stream.write(data);
//            return (path);
//        }
//
//
//    }

    public String encodeFileToBase64Binary(String filename) throws IOException {
        File file = new File(filename);
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        return new String(encoded, StandardCharsets.US_ASCII);
    }


    public String saveImage( String imageBase64,String fileName) throws IOException, FailedToUploadImageToAWSException {
        byte[] data = Base64.decodeBase64(imageBase64);

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.EU_WEST_1)
                .build();


        InputStream is = new ByteArrayInputStream(data);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(data.length);
        metadata.setLastModified(new Date());
        metadata.setContentType("image/jpeg");
        s3client.putObject(new PutObjectRequest(bucketName, fileName, is, metadata).withCannedAcl(CannedAccessControlList.PublicRead));

        S3Object s3Object = s3client.getObject(new GetObjectRequest(bucketName, fileName));
        logger.info("Image '" + fileName + "'  created. URL: " + s3Object.getObjectContent().getHttpRequest().getURI().toString());

        return s3Object.getObjectContent().getHttpRequest().getURI().toString();

    }
}
