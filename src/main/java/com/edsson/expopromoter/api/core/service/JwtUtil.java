package com.edsson.expopromoter.api.core.service;

import com.edsson.expopromoter.api.ExpopromoterProperties;
import com.edsson.expopromoter.api.config.SecretKeyProvider;
import com.edsson.expopromoter.api.context.UserContext;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import static java.time.ZoneOffset.UTC;

@Service
@ComponentScan
public class JwtUtil {

    private final ExpopromoterProperties expopromoterProperties;
    private final SecretKeyProvider secretKeyProvider;
    private static final String ISSUER = "com.edsson.sospes.jwt";
    @Autowired
    public JwtUtil(SecretKeyProvider secretKeyProvider, ExpopromoterProperties expopromoterProperties) {
        this.expopromoterProperties = expopromoterProperties;
        this.secretKeyProvider=secretKeyProvider;
    }

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, this.expopromoterProperties.getJwtSecret()).compact();
    }

    public Claims parseToken(String token) throws SignatureException {
        return Jwts.parser().setSigningKey(this.expopromoterProperties.getJwtSecret()).parseClaimsJws(token).getBody();
    }

    public String tokenFor(UserContext user) throws JwtException {
        try {
            byte[] secretKey = secretKeyProvider.getKey();
            Date expiration = Date.from(LocalDateTime.now(UTC).plusHours(2).toInstant(UTC));
//
            //   Date expiration = Date.from(LocalDateTime.now(UTC).plusMinutes(30).toInstant(UTC));


            String token = Jwts.builder()
                    .setSubject(user.getEmail())
                    .setExpiration(expiration)
                    .setIssuer(ISSUER)
                    .signWith(SignatureAlgorithm.HS512, secretKey)
                    .compact();
            return token;
        } catch (IOException | URISyntaxException e) {
            throw new JwtException(e.getMessage());
        }
    }
}