package com.edsson.expopromoter.api.core.service;

import com.edsson.expopromoter.api.ExpopromoterProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@ComponentScan
public class JwtUtil {

    private final ExpopromoterProperties expopromoterProperties;

    @Autowired
    public JwtUtil(ExpopromoterProperties expopromoterProperties) {
        this.expopromoterProperties = expopromoterProperties;
    }

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, this.expopromoterProperties.getJwtSecret()).compact();
    }

    public Claims parseToken(String token) throws SignatureException {
        return Jwts.parser().setSigningKey(this.expopromoterProperties.getJwtSecret()).parseClaimsJws(token).getBody();
    }
}