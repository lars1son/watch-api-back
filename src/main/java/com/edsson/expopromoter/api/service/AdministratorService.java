package com.edsson.expopromoter.api.service;

import com.edsson.expopromoter.api.exceptions.NoSuchEventPerUserException;
import com.edsson.expopromoter.api.exceptions.PrivilegiousException;
import com.edsson.expopromoter.api.model.EventDAO;
import com.edsson.expopromoter.api.model.Role;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorService {
    private final RoleRepository roleRepository;
    private final EventService eventService;
    @Autowired
    public AdministratorService(RoleRepository roleRepository, EventService eventService){
        this.roleRepository=roleRepository;
        this.eventService=eventService;
    }

    public void deleteEvent(int id, User user) throws PrivilegiousException, NoSuchEventPerUserException {
        if (user.getRole()== roleRepository.findRoleDAOByRole("ROLE_ADMIN")){
            EventDAO eventDAO=eventService.findOneById(id);
            eventService.deleteEvent(id,eventDAO.getUserCreatorId());
        }
        else throw new PrivilegiousException();
    }

}
