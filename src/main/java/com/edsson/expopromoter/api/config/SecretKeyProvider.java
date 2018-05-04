package com.edsson.expopromoter.api.config;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class SecretKeyProvider {
    public byte[] getKey() throws URISyntaxException, IOException {
        return "MangoWorms ".getBytes();
        //return Files.readAllBytes(Paths.get(this.getClass().getResource("/jwt.key").toURI()));
    }
}