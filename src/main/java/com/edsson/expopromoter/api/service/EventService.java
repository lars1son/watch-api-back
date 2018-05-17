package com.edsson.expopromoter.api.service;


import com.edsson.expopromoter.api.config.SystemConfigurationKeys;
import com.edsson.expopromoter.api.exceptions.EntityAlreadyExistException;
import com.edsson.expopromoter.api.exceptions.EventBadCredentialsException;
import com.edsson.expopromoter.api.exceptions.SystemConfigurationException;
import com.edsson.expopromoter.api.model.EventDAO;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.JsonEventInfo;
import com.edsson.expopromoter.api.operator.FileInfoService;
import com.edsson.expopromoter.api.operator.ImageOperator;
import com.edsson.expopromoter.api.repository.EventRepository;
import com.edsson.expopromoter.api.request.CreateEventRequest;
import com.edsson.expopromoter.api.service.system_configuration.SystemConfigurationServiceImpl;
import javassist.NotFoundException;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
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
    private final AmazonClient amazonClient;
    @Autowired
    public EventService(AmazonClient amazonClient,EventRepository eventRepository, SystemConfigurationServiceImpl systemConfigurationService, UserService userService, ImageOperator imageOperator) {
        this.repository = eventRepository;
        this.amazonClient=amazonClient;
        this.imageOperator = imageOperator;
        this.userService = userService;
        this.systemConfigurationService = systemConfigurationService;
    }

    public EventDAO findOneById(Integer id) {
        return repository.findById(id);
    }

    public void save(EventDAO eventDAO) {
        repository.save(eventDAO);
    }
//
//    public EventDAO findOneByName(String name) {
//        return repository.findByName(name);
//    }

    public String createEventDAO(CreateEventRequest createEventRequest, User user) throws IOException, SystemConfigurationException, ParseException, EntityAlreadyExistException, EventBadCredentialsException, NotFoundException {

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
                String path = systemConfigurationService.getValueByKey(SystemConfigurationKeys.DefaultImagePath.PATH) + "\\event_" + eventDAO.getId();
//For local storage
//                eventDAO.setPhotoPath(imageOperator.saveImage(createEventRequest.getImageBase64(),path, eventDAO.getName()));
//               For amazon
                eventDAO.setPhotoPath(amazonClient.uploadFileTos3bucket(createEventRequest.getImageBase64(),eventDAO.getName()));
                userService.save(user);

                eventDAO = repository.findByName(eventDAO.getName());
                return buildUrl(eventDAO.getId());
            } else throw new EntityAlreadyExistException("Event with this name has already existed");
        } else throw new NotFoundException("User not found!");
    }

    public void update(CreateEventRequest eventDAO,HttpServletRequest request) throws NotFoundException {
//        User user = userService.findOneByEmail(eventDAO.get);
        User user = (User) request.getAttribute("user");
        if (user != null) {

            EventDAO savedEvent = repository.findById(eventDAO.getId());
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
                throw new NotFoundException("User " + user.getEmail() + " does not have event " + savedEvent.getName() + "!");
        } else throw new NotFoundException("User " + user.getEmail() + " not found!");
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
