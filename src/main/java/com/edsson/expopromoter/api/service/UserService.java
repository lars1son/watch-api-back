package com.edsson.expopromoter.api.service;

import com.edsson.expopromoter.api.config.RolesConfiguration;
import com.edsson.expopromoter.api.config.SystemConfigurationKeys;
import com.edsson.expopromoter.api.context.UserContext;
import com.edsson.expopromoter.api.exceptions.*;
import com.edsson.expopromoter.api.model.EventDAO;
import com.edsson.expopromoter.api.model.PasswordResetTokenDAO;
import com.edsson.expopromoter.api.model.RoleDAO;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.JsonTicket;
import com.edsson.expopromoter.api.operator.ImageOperator;
import com.edsson.expopromoter.api.operator.MailSender;
import com.edsson.expopromoter.api.repository.EventRepository;
import com.edsson.expopromoter.api.repository.PasswordTokenRepository;
import com.edsson.expopromoter.api.repository.UserRepository;
import com.edsson.expopromoter.api.request.LoginRequest;
import com.edsson.expopromoter.api.request.MergeRequest;
import com.edsson.expopromoter.api.request.RegisterDeviceRequest;
import com.edsson.expopromoter.api.request.UserUpdateRequest;
import com.edsson.expopromoter.api.service.system_configuration.SystemConfigurationService;
import com.edsson.expopromoter.api.service.system_configuration.SystemConfigurationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class UserService {

    private final UserRepository repository;

    private final EventRepository eventRepository;

    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImageOperator imageOperator;
    private final MailSender mailSender;
    private final SystemConfigurationService systemConfigurationService;
    private final PasswordTokenRepository passwordTokenRepository;

    //    private final EventService eventService;
    @Autowired
    public UserService(EventRepository eventRepository,
                       PasswordTokenRepository passwordTokenRepository, SystemConfigurationServiceImpl systemConfigurationService, MailSender mailSender, UserRepository userRepository, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder, ImageOperator imageOperator) {
        this.repository = userRepository;
        this.systemConfigurationService = systemConfigurationService;
        this.roleService = roleService;
        this.imageOperator = imageOperator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailSender = mailSender;
        this.passwordTokenRepository = passwordTokenRepository;
        this.eventRepository = eventRepository;
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

    public UserContext getUser(String email) throws NoSuchUserException {
        User user = repository.findOneByEmail(email);
        if (user != null) {

            return UserContext.create(user);
        } else {
            throw new NoSuchUserException();
        }
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

    public void create(String password, String email) throws EntityAlreadyExistException {
        RoleDAO roleDAO = roleService.findRoleDAOByRole(RolesConfiguration.ROLE_USER);

        User user = new User();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(roleDAO);

        if (repository.findOneByEmail(user.getEmail()) == null) {
            repository.save(user);
        } else throw new EntityAlreadyExistException();
    }

    public void create(RegisterDeviceRequest registerDeviceRequest) throws EntityAlreadyExistException {
        RoleDAO roleDAO = roleService.findRoleDAOByRole(RolesConfiguration.ROLE_USER);
        User user = new User();
        user.setEmail(registerDeviceRequest.getDeviceId());
        user.setRole(roleDAO);
        if (repository.findOneByEmail(user.getEmail()) == null) {
            repository.save(user);
        } else throw new EntityAlreadyExistException();
    }

    public void save(User user) {
        repository.save(user);
    }


    public Set<JsonTicket> getAllTickets(HttpServletRequest request) throws IOException {
        User u = (User) request.getAttribute("user");
        List<JsonTicket> tickets = new ArrayList<>();

//        for (TicketDAO ticketDAO : u.getTicketList()) {
//            tickets.add(new JsonTicket(ticketDAO.getId(), ticketDAO.getEventsByEventId().getName(), imageOperator.encodeFileToBase64Binary(ticketDAO.getImagePath())));
//        }
//        return tickets;
        return u.getTickets();
    }

    public void update(UserUpdateRequest request, User user) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {

        for (Method m : UserUpdateRequest.class.getDeclaredMethods()) {


            if (m.getName().startsWith("get") && m.getParameterCount() == 0 && !m.getName().startsWith("getNew")) {
                m.setAccessible(true);

                Method getUserMethod = User.class.getDeclaredMethod(m.getName());
                getUserMethod.setAccessible(true);
                if ((String) m.invoke(request) != null) {
                    if ((String) getUserMethod.invoke(user) != null) {
                        if (((String) getUserMethod.invoke(user)).toLowerCase().equals((m.invoke(request)))) {
                            continue;
                        }
                    }
                    Method setMethod = User.class.getDeclaredMethod(m.getName().replace("get", "set"), String.class);
                    setMethod.invoke(user, m.invoke(request));

                }
            }
        }


        repository.save(user);
    }
//
//    public String newTokenIfOldIsExpired(String token) throws IOException, URISyntaxException {
//        return jwtUtil.updateToken(token);
//    }

    private int getPasswordExpirationHours() throws SystemConfigurationException {
        Object object = systemConfigurationService.getValueByKey(SystemConfigurationKeys.Password.RESET_LINK_TIMEOUT);
        return Integer.parseInt((String) object);
    }

    public String createPasswordResetTokenForUser(User user) throws FailedToCreateResetPasswordTokenException {
        try {
            String token = UUID.randomUUID().toString();
            PasswordResetTokenDAO myToken = new PasswordResetTokenDAO(user, token, getPasswordExpirationHours());
            passwordTokenRepository.save(myToken);
            return token;
        } catch (Exception ex) {
            throw new FailedToCreateResetPasswordTokenException();
        }
    }


    public UserContext findUserByResetPasswordToken(String resetPasswordToken) {
        PasswordResetTokenDAO passwordResetTokenDAO = passwordTokenRepository.findPasswordResetTokenDAOByToken(resetPasswordToken);
        if (passwordResetTokenDAO != null) {
            return UserContext.create(passwordResetTokenDAO.getUser());
        }
        return null;
    }

    public void updateUserEmail(UserContext userContext, String newEmail, String updateEmailToken) throws FailedToUpdateUserException {
        User userDAO = repository.findOneById(userContext.getUserId());

        PasswordResetTokenDAO tokenDAO = passwordTokenRepository.findPasswordResetTokenDAOByToken(updateEmailToken);
        if (userDAO == null) {
            throw new FailedToUpdateUserException("User does not exist");
        }
        if (tokenDAO == null) {
            throw new FailedToUpdateUserException("Token does not exist");
        }
        if (tokenDAO.getExpiryDate().before(new Date())) {
            throw new FailedToUpdateUserException("Token expired");
        }
        userDAO.setEmail(newEmail);
        repository.save(userDAO);

    }

    public void updateUserPassword(UserContext userContext, String newPassword, String updatePasswordToken) throws FailedToUpdateUserException {
        User userDAO = repository.findOneById(userContext.getUserId());

        PasswordResetTokenDAO tokenDAO = passwordTokenRepository.findPasswordResetTokenDAOByToken(updatePasswordToken);
        if (userDAO == null) {
            throw new FailedToUpdateUserException("User does not exist");
        }
        if (tokenDAO == null) {
            throw new FailedToUpdateUserException("Token does not exist");
        }
        if (tokenDAO.getExpiryDate().before(new Date())) {
            throw new FailedToUpdateUserException("Token expired");
        }
        userDAO.setPassword(bCryptPasswordEncoder.encode(newPassword));
        repository.save(userDAO);
    }

    public void merge(User user, MergeRequest mergeRequest) throws EntityAlreadyExistException {

        create(mergeRequest.getPassword(), mergeRequest.getEmail());

        User newUser = repository.findOneByEmail(mergeRequest.getEmail());

//        Set<EventDAO> eventDAOlist = user.getEventDAOList();
//        newUser.setEventDAOList();
//        user.setEventDAOList(null);
//        repository.save(user);
//        repository.save(newUser);
//
        for (EventDAO eventDAO : user.getEvents()) {
            eventDAO.setUserCreatorId(newUser.getId());
            eventRepository.save(eventDAO);
        }


    }

    public void addEvent(int id, User user) throws EntityNotFoundException {
        EventDAO eventDAO = eventRepository.findById(id);
        if (eventDAO != null) {
            user.getEvents().add(eventDAO);
        }
        else {
            throw new EntityNotFoundException();
        }
    }
}

