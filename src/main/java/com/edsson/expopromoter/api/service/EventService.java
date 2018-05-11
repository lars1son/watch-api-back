package com.edsson.expopromoter.api.service;


import com.edsson.expopromoter.api.config.SystemConfigurationKeys;
import com.edsson.expopromoter.api.exceptions.EntityAlreadyExistException;
import com.edsson.expopromoter.api.exceptions.EventBadCredentialsException;
import com.edsson.expopromoter.api.exceptions.SystemConfigurationException;
import com.edsson.expopromoter.api.model.EventDAO;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.JsonEventInfo;
import com.edsson.expopromoter.api.operator.ImageOperator;
import com.edsson.expopromoter.api.repository.EventRepository;
import com.edsson.expopromoter.api.request.CreateEventRequest;
import com.edsson.expopromoter.api.service.system_configuration.SystemConfigurationServiceImpl;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class EventService {

    private final EventRepository repository;
    private final ImageOperator imageOperator;
    private final UserService userService;
    private final SystemConfigurationServiceImpl systemConfigurationService;

    @Autowired
    public EventService(EventRepository eventRepository, SystemConfigurationServiceImpl systemConfigurationService, UserService userService, ImageOperator imageOperator) {
        this.repository = eventRepository;
        this.imageOperator = imageOperator;
        this.userService = userService;
        this.systemConfigurationService = systemConfigurationService;
    }

    public EventDAO findOneById(Integer id) {
        return repository.findById(id);
    }

    public void create(EventDAO eventDAO) {
        repository.save(eventDAO);
    }
//
//    public EventDAO findOneByName(String name) {
//        return repository.findByName(name);
//    }

    public String createEventDAO(CreateEventRequest createEventRequest) throws IOException, SystemConfigurationException, ParseException, EntityAlreadyExistException, EventBadCredentialsException, NotFoundException {
        User user = userService.findOneByEmail(createEventRequest.getUserEmail());
        if (user != null) {
            if (repository.findByName(createEventRequest.getName()) == null) {

                EventDAO eventDAO = new EventDAO();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

                if (createEventRequest.getName() != null && createEventRequest.getDateStart() != null && createEventRequest.getDateEnd() != null) {
                    eventDAO.setName(createEventRequest.getName());
                    eventDAO.setDateStart(formatter.parse(createEventRequest.getDateStart()));
                    eventDAO.setDateEnd(formatter.parse(createEventRequest.getDateEnd()));

                } else throw new EventBadCredentialsException("Bad Credentials");

//            createEventRequest.getImageBase64();

                eventDAO.setAgenda(createEventRequest.getAgenda());
                eventDAO.setContacts(createEventRequest.getContacts());
                eventDAO.setDescription(createEventRequest.getDescription());
                eventDAO.setEventLocation(createEventRequest.getLocation());
                eventDAO.setEventWebsite(createEventRequest.getWebsite());
                eventDAO.setTicketUrl(createEventRequest.getTicketUrl());
                eventDAO.setUserCreatorId(user);
                user.addToEventDAOList(eventDAO);

                eventDAO.setPhotoPath(imageOperator.saveImage(createEventRequest.getImageBase64(), eventDAO.getId()));
                userService.save(user);

                eventDAO = repository.findByName(eventDAO.getName());
                return buildUrl(eventDAO.getId());
            } else throw new EntityAlreadyExistException("Event with this name has already existed");
        } else throw new NotFoundException("User " + createEventRequest.getUserEmail() + " not found!");
    }

    public void update(CreateEventRequest eventDAO) throws NotFoundException {
        User user = userService.findOneByEmail(eventDAO.getUserEmail());
        if (user != null) {
            EventDAO savedEvent = repository.findByName(eventDAO.getName());
            if (user.getEventDAOList().contains(savedEvent)) {

                user.deleteRecordFromEventDAOList(savedEvent);

                if (eventDAO.getTicketUrl() != null) {
                    savedEvent.setTicketUrl(eventDAO.getTicketUrl());
                }
                if (eventDAO.getWebsite() != null) {
                    savedEvent.setEventWebsite(eventDAO.getWebsite());
                }
                if (eventDAO.getLocation() != null) {
                    savedEvent.setEventLocation(eventDAO.getLocation());
                }
                if (eventDAO.getDescription() != null) {
                    savedEvent.setDescription(eventDAO.getDescription());
                }
                if (eventDAO.getAgenda() != null) {
                    savedEvent.setAgenda(eventDAO.getAgenda());
                }
                if (eventDAO.getContacts() != null) {
                    savedEvent.setContacts(eventDAO.getContacts());
                }
                user.addToEventDAOList(savedEvent);
//                repository.save(savedEvent);
                userService.save(user);
            } else
                throw new NotFoundException("User " + eventDAO.getUserEmail() + " does not have event " + savedEvent.getName() + "!");
        } else throw new NotFoundException("User " + eventDAO.getUserEmail() + " not found!");
    }

    public String buildUrl(int id) throws SystemConfigurationException {
        return systemConfigurationService.getValueByKey(SystemConfigurationKeys.DefaultEventURL.URL) + id;
    }
    public JsonEventInfo buildWithImage(EventDAO eventDAO) throws IOException {
        String photo = eventDAO.getPhotoPath();
        photo=imageOperator.encodeFileToBase64Binary(photo);
        JsonEventInfo json = JsonEventInfo.from(eventDAO);
        json.setPhoto(photo);
        return json;
    }
}
