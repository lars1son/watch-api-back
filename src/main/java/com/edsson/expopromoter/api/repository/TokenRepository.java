package com.edsson.expopromoter.api.repository;

import com.edsson.expopromoter.api.model.TokenDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<TokenDAO, String> {

    TokenDAO findOneByToken(String s);

    void deleteByToken(String token);
}
