package com.edsson.expopromoter.api.service;

import com.edsson.expopromoter.api.config.RolesConfiguration;
import com.edsson.expopromoter.api.context.UserContext;
import com.edsson.expopromoter.api.exceptions.EntityAlreadyExistException;
import com.edsson.expopromoter.api.model.RoleDAO;
import com.edsson.expopromoter.api.model.TicketDAO;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.JsonTicket;
import com.edsson.expopromoter.api.operator.ImageOperator;
import com.edsson.expopromoter.api.repository.UserRepository;
import com.edsson.expopromoter.api.request.LoginRequest;
import com.edsson.expopromoter.api.request.RegisterDeviceRequest;
import com.edsson.expopromoter.api.request.RegistrationRequest;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImageOperator imageOperator;
    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder, ImageOperator imageOperator) {
        this.repository = userRepository;
        this.roleService = roleService;
        this.imageOperator=imageOperator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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

    public UserContext getUser(String email) {
        User user = repository.findOneByEmail(email);
        if (user != null) {

            return UserContext.create(user);
        }
        return null;
    }

    public User findOneByEmail(String email) {
        return repository.findOneByEmail(email);
    }

    public User findOneById(Long id) {
        return repository.findOneById(id);
    }

    public User update(User u) {
        return repository.save(u);
    }

    public void create(RegistrationRequest registrationRequest) throws EntityAlreadyExistException {
        RoleDAO roleDAO = roleService.findRoleDAOByRole(RolesConfiguration.ROLE_USER);
        User user = new User();
        user.setPassword(bCryptPasswordEncoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());
        user.setRole(roleDAO);
        java.util.Date dt = new java.util.Date();
//        user.setCreatedAt(dt);
//        user.setUpdatedAt(dt);
        if (repository.findOneByEmail(user.getEmail()) == null) {
            repository.save(user);
        } else throw new EntityAlreadyExistException("User " + user.getEmail() + "already exists");
    }

    public void create(RegisterDeviceRequest registerDeviceRequest) throws EntityAlreadyExistException {
        RoleDAO roleDAO = roleService.findRoleDAOByRole(RolesConfiguration.ROLE_USER);
        User user = new User();
        user.setEmail(registerDeviceRequest.getDeviceId());
        user.setRole(roleDAO);
        if (repository.findOneByEmail(user.getEmail()) == null) {
            repository.save(user);
        } else throw new EntityAlreadyExistException("User " + user.getEmail() + "already exists");
    }

    public void save(User user) {
        repository.save(user);
    }


    public List<JsonTicket> getAllTickets(HttpServletRequest request) throws IOException {
        User u = (User) request.getAttribute("user");
        List<JsonTicket> tickets = new ArrayList<>();

        for (TicketDAO ticketDAO : u.getTickets()) {
            tickets.add(new JsonTicket(ticketDAO.getId(),ticketDAO.getEventsByEventId().getName(),imageOperator.encodeFileToBase64Binary(ticketDAO.getImagePath())));
        }
        return tickets;
    }

}

