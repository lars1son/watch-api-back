package com.edsson.expopromoter.api.core.service;

import com.edsson.expopromoter.api.ExpopromoterProperties;
import com.edsson.expopromoter.api.config.SecretKeyProvider;
import com.edsson.expopromoter.api.context.UserContext;
import com.edsson.expopromoter.api.model.Credential;
import com.edsson.expopromoter.api.model.TokenDAO;
import com.edsson.expopromoter.api.repository.TokenRepository;
import com.edsson.expopromoter.api.service.UserService;
import io.jsonwebtoken.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static java.time.ZoneOffset.UTC;

@Service
@ComponentScan
public class JwtUtil {
    private static final Logger logger = Logger.getLogger(JwtUtil.class);

    private final SecretKeyProvider secretKeyProvider;
    private static final String ISSUER = "com.edsson.sospes.jwt";
    private final UserService userService;
    private final TokenRepository tokenRepository;

    @Autowired
    public JwtUtil(TokenRepository tokenRepository, UserService userService, SecretKeyProvider secretKeyProvider, ExpopromoterProperties expopromoterProperties) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.secretKeyProvider = secretKeyProvider;
    }

//    public String generateToken(Map<String, Object> claims) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .signWith(SignatureAlgorithm.HS512, this.expopromoterProperties.getJwtSecret()).compact();
//    }

    public Claims parseToken(String token) throws SignatureException, IOException, URISyntaxException {

//        return Jwts.parser().setSigningKey(secretKeyProvider.getKey()).parseClaimsJws(updateToken(token)).getBody();
        return Jwts.parser().setSigningKey(secretKeyProvider.getKey()).parseClaimsJws(token).getBody();
    }

    public String tokenFor(UserContext user) throws JwtException {
        try {
            byte[] secretKey = secretKeyProvider.getKey();
            Date expiration = Date.from(LocalDateTime.now(UTC).plusHours(2).toInstant(UTC));
            String token = Jwts.builder()
                    .setSubject(user.getEmail())
                    .setExpiration(expiration)
                    .setIssuer(ISSUER)
                    .claim("user", new Credential(user))
                    .signWith(SignatureAlgorithm.HS512, secretKey)
                    .compact();
            TokenDAO tokenDAO = new TokenDAO(token);

            tokenRepository.save(tokenDAO);

            return token;
        } catch (IOException | URISyntaxException e) {
            throw new JwtException(e.getMessage());
        }
    }

    public String updateToken(String token) throws IOException, URISyntaxException {
        byte[] secretKey = secretKeyProvider.getKey();
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        UserContext user = UserContext.create(userService.findOneByEmail(claims.getBody().getSubject()));

        Date expirationDate = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
        LocalDateTime ldt = LocalDateTime.ofInstant(expirationDate.toInstant(), ZoneId.systemDefault());

        if (ldt.isBefore(LocalDateTime.now())) {
            tokenRepository.deleteByToken(token);
            logger.info("Expiration date is in 10 minutes. New token will be returned.");
            return tokenFor(user);
        } else {
            logger.info("Expiration date is OK. ");
            return token;
        }
    }
}