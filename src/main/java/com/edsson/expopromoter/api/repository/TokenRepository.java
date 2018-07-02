package com.edsson.expopromoter.api.repository;

import com.edsson.expopromoter.api.model.TokenDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<TokenDAO, String> {

    TokenDAO findOneByToken(String s);

    void deleteByToken(String token);
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TokenDAO t WHERE t.token = :token")
    boolean existByToken(@Param("token") String token);

    TokenDAO findOneByUser_Id(Long id);
}
