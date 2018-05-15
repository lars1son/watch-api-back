package com.edsson.expopromoter.api.service;


import com.edsson.expopromoter.api.config.SystemConfigurationKeys;
import com.edsson.expopromoter.api.exceptions.EntityAlreadyExistException;
import com.edsson.expopromoter.api.exceptions.SystemConfigurationException;
import com.edsson.expopromoter.api.model.EventDAO;
import com.edsson.expopromoter.api.model.TicketDAO;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.operator.FileInfoService;
import com.edsson.expopromoter.api.operator.ImageOperator;
import com.edsson.expopromoter.api.repository.TicketRepository;
import com.edsson.expopromoter.api.request.AddTicketRequest;
import com.edsson.expopromoter.api.service.system_configuration.SystemConfigurationServiceImpl;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TicketService {

    private final TicketRepository repository;
    private final SystemConfigurationServiceImpl systemConfigurationService;
    private final EventService eventService;
    private final ImageOperator imageOperator;
    private final UserService userService;

    @Autowired
    public TicketService(TicketRepository ticketRepository, ImageOperator imageOperator, SystemConfigurationServiceImpl systemConfigurationService, UserService userService, EventService eventService) {
        this.repository = ticketRepository;
        this.eventService = eventService;
        this.systemConfigurationService = systemConfigurationService;
        this.imageOperator = imageOperator;
        this.userService = userService;
    }

    public TicketDAO findOneById(String id) {
        return repository.findOne(id);
    }


    public int addUserTicketFoEvent(AddTicketRequest addTicketRequest, HttpServletRequest request) throws SystemConfigurationException, IOException, EntityAlreadyExistException {
        TicketDAO ticket = new TicketDAO();
        User user = (User) request.getAttribute("user");
        if (!user.getTickets().contains(ticket)) {
            String path = systemConfigurationService.getValueByKey(SystemConfigurationKeys.DefaultImagePath.PATH) + "\\user_" + user.getId().intValue();
            EventDAO eventDAO = eventService.findOneById(addTicketRequest.getEventId());
            path = path + "\\" + eventDAO.getName() + "_" + FileInfoService.findFileExtension(addTicketRequest.getImageBase64());

            imageOperator.saveImage(addTicketRequest.getImageBase64(), path );

            ticket.setImagePath(path);
            ticket.setUser(user);
            ticket.setEventsByEventId(eventDAO);
            repository.save(ticket);
//
//            eventDAO.addTicket(ticket);
//            user.addToTicketDAOList(ticket);
//
//            userService.save(user);
            System.out.println("============================= count: "+ eventDAO.getTickets().size());
//            eventService.save(eventDAO);
            return ticket.getId();
        } else throw new EntityAlreadyExistException("Ticket's already existed for this user");
    }
}
