package com.edsson.expopromoter.api.repository;


import com.edsson.expopromoter.api.model.PasswordResetTokenDAO;
import com.edsson.expopromoter.api.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetTokenDAO, Long> {


    PasswordResetTokenDAO findDistinctByUser(User user);

    PasswordResetTokenDAO findPasswordResetTokenDAOByToken(String token);
}
