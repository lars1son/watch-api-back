package com.edsson.expopromoter.api.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.edsson.expopromoter.api.config.SystemConfigurationKeys;
import com.edsson.expopromoter.api.exceptions.SystemConfigurationException;
import com.edsson.expopromoter.api.service.system_configuration.SystemConfigurationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

@Service
public class AmazonClient {

    private AmazonS3 s3client;

    @Value("${amazon.endpointUrl}")
    private String endpointUrl;

    @Value("${amazon.bucketName}")
    private String bucketName;

    @Value("${amazon.accessKey}")
    private String accessKey;

    @Value("${amazon.secretKey}")
    private String secretKey;

    private final SystemConfigurationServiceImpl systemConfigurationService;
    @Autowired
    public  AmazonClient(SystemConfigurationServiceImpl systemConfigurationService){
        this.systemConfigurationService=systemConfigurationService;

    }
    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }


    public String uploadFileTos3bucket(String base64Data, String fileName) {

        byte[] bI = org.apache.commons.codec.binary.Base64.decodeBase64((base64Data.substring(base64Data.indexOf(",")+1)).getBytes());
        InputStream fis = new ByteArrayInputStream(bI);


        Region usWest02 = Region.getRegion(Regions.CA_CENTRAL_1);
        s3client.setRegion(usWest02);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bI.length);
        metadata.setContentType("image/png");
        metadata.setCacheControl("public, max-age=31536000");
        s3client.putObject(bucketName, fileName, fis, metadata);
        s3client.setObjectAcl(bucketName, fileName, CannedAccessControlList.PublicRead);


        return fileName;
    }


    public String deleteFileFromS3Bucket(String fileName) throws SystemConfigurationException {
//        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        fileName = systemConfigurationService.getValueByKey(SystemConfigurationKeys.DefaultImagePath.PATH)+"/"+fileName;
        s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
        return "Successfully deleted";
    }
}