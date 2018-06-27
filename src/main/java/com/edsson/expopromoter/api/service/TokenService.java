package com.edsson.expopromoter.api.service;

import com.edsson.expopromoter.api.model.TokenDAO;
import com.edsson.expopromoter.api.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository=tokenRepository;
    }

    public String getToken(String token){
        TokenDAO tokenDAO = tokenRepository.findOneByToken(token);
        return tokenDAO.getToken();
    }
}
