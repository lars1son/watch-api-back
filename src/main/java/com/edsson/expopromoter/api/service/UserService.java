package com.edsson.expopromoter.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.repository.UserRepository;
import com.edsson.expopromoter.api.request.LoginRequest;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.repository = userRepository;
    }

    /**
     * Login com.edsson.expopromoter.api.user amd return the com.edsson.expopromoter.api.user details.
     *
     * @param loginRequest - login request
     * @return return the com.edsson.expopromoter.api.user details and jwt in the header.
     */
    public User login(LoginRequest loginRequest) {
        User u = repository.findOneByEmail(loginRequest.getEmail());
        if (u != null) {
            boolean isPasswordMatch = new BCryptPasswordEncoder().matches(loginRequest.getPassword(), u.getPassword());
            if (isPasswordMatch) {
                return u;
            }
        }
        return null;
    }
    public User getUser(String email){
        User u = repository.findOneByEmail(email);
        if (u != null) {
                return u;
        }
        return null;
    }
    public User findOneById(String id) {
        return repository.findOne(id);
    }

    public User update(User u) {
        return repository.save(u);
    }

    public void create(User u) {
         repository.save(u);
    }
}

