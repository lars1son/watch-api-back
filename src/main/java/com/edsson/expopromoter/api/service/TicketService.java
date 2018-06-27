package com.edsson.expopromoter.api.service;


import com.edsson.expopromoter.api.context.Messages;
import com.edsson.expopromoter.api.exceptions.*;
import com.edsson.expopromoter.api.model.TicketDAO;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.GenericResponse;
import com.edsson.expopromoter.api.operator.ImageOperator;
import com.edsson.expopromoter.api.repository.TicketRepository;
import com.edsson.expopromoter.api.request.AddTicketRequest;
import com.edsson.expopromoter.api.service.system_configuration.SystemConfigurationServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public class TicketService {
    private static final Logger log = Logger.getLogger(TicketService.class);
    private final TicketRepository repository;
    private final SystemConfigurationServiceImpl systemConfigurationService;
    private final EventService eventService;
    private final ImageOperator imageOperator;
    private final UserService userService;
    private final AmazonClient amazonClient;

    @Autowired
    public TicketService(AmazonClient amazonClient, TicketRepository ticketRepository, ImageOperator imageOperator, SystemConfigurationServiceImpl systemConfigurationService, UserService userService, EventService eventService) {
        this.repository = ticketRepository;
        this.amazonClient = amazonClient;
        this.eventService = eventService;
        this.systemConfigurationService = systemConfigurationService;
        this.imageOperator = imageOperator;
        this.userService = userService;
    }

    public TicketDAO findOneById(String id) {
        return repository.findOne(id);
    }


    public int addUserTicketFoEvent(AddTicketRequest addTicketRequest, HttpServletRequest request) throws SystemConfigurationException, IOException, EntityAlreadyExistException, FailedToUploadImageToAWSException, EntityNotFoundException {
        User user = (User) request.getAttribute("user");
        if (addTicketRequest.getEventId() > 0) {
            TicketDAO ticket = new TicketDAO();

            ticket.setEventsByEventId(eventService.findOneById(addTicketRequest.getEventId()));
            ticket.setUser(userService.findOneById(user.getId()));

            String name = String.valueOf("ticket_user_" + user.getId().intValue() + "_for_event_" + ticket.getEventsByEventId().getId());
            if (addTicketRequest.getImageBase64() != null) {
                ticket.setImagePath(imageOperator.saveImage(addTicketRequest.getImageBase64(), name));
            }
            repository.save(ticket);
            log.info("Ticket created for event " + ticket.getEventsByEventId().getId());
            return ticket.getId();
        } else {
            throw new EntityNotFoundException();
        }


    }

    @Transactional
    public GenericResponse deleteTicket(int id, HttpServletRequest request) throws NoSuchTicketPerUserException {
        TicketDAO ticket = repository.findOneById(id);
        User user = (User) request.getAttribute("user");


        for (TicketDAO ticketDAO : user.getTicketList()) {
            if (ticket.equals(ticketDAO)) {
                repository.removeById(id);
                return new GenericResponse(Messages.MESSAGE_DELETE_TICKET_SUCCESS, new String[]{});
            }
        }

        throw new NoSuchTicketPerUserException(user.getEmail());
    }

}
