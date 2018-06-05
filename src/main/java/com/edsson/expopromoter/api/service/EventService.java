package com.edsson.expopromoter.api.service;


import com.edsson.expopromoter.api.config.SystemConfigurationKeys;
import com.edsson.expopromoter.api.exceptions.EntityAlreadyExistException;
import com.edsson.expopromoter.api.exceptions.EventBadCredentialsException;
import com.edsson.expopromoter.api.exceptions.NoSuchEventPerUserException;
import com.edsson.expopromoter.api.exceptions.SystemConfigurationException;
import com.edsson.expopromoter.api.model.EventDAO;
import com.edsson.expopromoter.api.model.GpsDAO;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.JsonEventInfo;
import com.edsson.expopromoter.api.model.json.JsonUrl;
import com.edsson.expopromoter.api.operator.ImageOperator;
import com.edsson.expopromoter.api.repository.EventRepository;
import com.edsson.expopromoter.api.repository.GPSRepository;
import com.edsson.expopromoter.api.request.AddGPSRequest;
import com.edsson.expopromoter.api.request.CreateEventRequest;
import com.edsson.expopromoter.api.request.GetUpdatedEventsRequest;
import com.edsson.expopromoter.api.service.system_configuration.SystemConfigurationServiceImpl;
import javassist.NotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private static final Logger logger = Logger.getLogger(EventService.class);

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final EventRepository repository;
    private final ImageOperator imageOperator;
    private final UserService userService;
    private final SystemConfigurationServiceImpl systemConfigurationService;
    private final AmazonClient amazonClient;
    private final GPSRepository gpsRepository;

    @Autowired
    public EventService(GPSRepository gpsRepository, AmazonClient amazonClient, EventRepository eventRepository, SystemConfigurationServiceImpl systemConfigurationService, UserService userService, ImageOperator imageOperator) {
        this.repository = eventRepository;
        this.amazonClient = amazonClient;
        this.imageOperator = imageOperator;
        this.gpsRepository=gpsRepository;
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

    public JsonUrl createEventDAO(CreateEventRequest createEventRequest, User user) throws IOException, SystemConfigurationException, ParseException, EntityAlreadyExistException, EventBadCredentialsException, NotFoundException {
        if (user != null) {
            if (repository.findByName(createEventRequest.getName()) == null) {

                EventDAO eventDAO = new EventDAO();


                if (createEventRequest.getName() != null && createEventRequest.getDateStart() != null && createEventRequest.getDateEnd() != null) {
                    eventDAO.setName(createEventRequest.getName());
                    eventDAO.setDateStart(formatter.parse(createEventRequest.getDateStart()));
                    eventDAO.setDateEnd(formatter.parse(createEventRequest.getDateEnd()));

                } else throw new EventBadCredentialsException("Bad Credentials");

                eventDAO.setAgenda(createEventRequest.getAgenda());
                eventDAO.setContacts(createEventRequest.getContacts());
                eventDAO.setDescription(createEventRequest.getDescription());
                eventDAO.setEventLocation(createEventRequest.getLocation());
                eventDAO.setEventWebsite(createEventRequest.getWebsite());
                eventDAO.setTicketUrl(createEventRequest.getTicketUrl());
                eventDAO.setUserCreatorId(user);

//               For amazon
//                if (createEventRequest.getImageBase64() != null) {
//                    eventDAO.setPhotoPath(amazonClient.uploadFileTos3bucket(createEventRequest.getImageBase64(), eventDAO.getName()));

//                }

                user.addToEventDAOList(eventDAO);
                userService.save(user);


                logger.info(createEventRequest.getCoverImageBase64());


                logger.info("New event saved! ");
                eventDAO = repository.findByName(eventDAO.getName());

                eventDAO.setEventInfoUrl(systemConfigurationService.getValueByKey(SystemConfigurationKeys.EventInfoURL.URL) + eventDAO.getId());

                if (createEventRequest.getCoverImageBase64() != null) {
                    //For local storage
//                String path = systemConfigurationService.getValueByKey(SystemConfigurationKeys.DefaultImagePath.PATH) + "\\event_" + eventDAO.getId();
//              AMAZON

                    String path = systemConfigurationService.getValueByKey(SystemConfigurationKeys.DefaultImagePath.PATH) + "cover_photo_" + eventDAO.getId();
                    eventDAO.setPhotoPath(imageOperator.saveImage(createEventRequest.getCoverImageBase64(), path));
                }
                if (createEventRequest.getInfoImageBase64() != null) {
                    String path = systemConfigurationService.getValueByKey(SystemConfigurationKeys.DefaultImagePath.PATH) + "info_photo_" + eventDAO.getId();
                    eventDAO.setInfoPhotoPath(imageOperator.saveImage(createEventRequest.getInfoImageBase64(), path));
                }


                repository.save(eventDAO);

                return new JsonUrl(eventDAO.getPhotoPath(), eventDAO.getInfoPhotoPath(), eventDAO.getId(), eventDAO.getEventInfoUrl());
            } else {
                logger.error(new EntityAlreadyExistException("Event with this name has already existed"));
                throw new EntityAlreadyExistException("Event with this name has already existed");
            }

        } else {
            logger.error(new NotFoundException("User not found!"));
            throw new NotFoundException("User not found!");
        }
    }

    public JsonUrl update(CreateEventRequest eventDAO, HttpServletRequest request) throws NotFoundException, SystemConfigurationException, IOException, ParseException {

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
                if (eventDAO.getCoverImageBase64() != null) {
//                    Windows
//                    String path = systemConfigurationService.getValueByKey(SystemConfigurationKeys.DefaultImagePath.PATH) + "\\event_" + eventDAO.getId();
//                    Linux
                    String path = systemConfigurationService.getValueByKey(SystemConfigurationKeys.DefaultImagePath.PATH) + "/cover_" + eventDAO.getId();
                    savedEvent.setPhotoPath(imageOperator.saveImage(eventDAO.getCoverImageBase64(), path));
                }
                if (eventDAO.getInfoImageBase64() != null) {
                    String path = systemConfigurationService.getValueByKey(SystemConfigurationKeys.DefaultImagePath.PATH) + "/event_" + eventDAO.getId();
                    savedEvent.setPhotoPath(imageOperator.saveImage(eventDAO.getInfoImageBase64(), path));

                }
                if (eventDAO.getDateEnd() != null) {
                    savedEvent.setDateEnd(formatter.parse(eventDAO.getDateEnd()));
                }
                if (eventDAO.getDateStart() != null) {
                    savedEvent.setDateStart(formatter.parse(eventDAO.getDateStart()));
                }
                if (eventDAO.getName() != null) {
                    savedEvent.setName(eventDAO.getName());
                }

                user.addToEventDAOList(savedEvent);
                userService.save(user);
                return new JsonUrl(savedEvent.getPhotoPath(), savedEvent.getInfoPhotoPath(), savedEvent.getId(), savedEvent.getEventInfoUrl());
            } else
                throw new NotFoundException("User " + user.getEmail() + " does not have event: '" + savedEvent.getName() + "'!");
        } else throw new NotFoundException("User " + user.getEmail() + " not found!");
    }

//
//    public String buildUrl(int id) throws SystemConfigurationException {
//        return systemConfigurationService.getValueByKey(SystemConfigurationKeys.EventInfoURL.URL) + id;
//    }


    public JsonEventInfo buildWithImage(EventDAO eventDAO) throws IOException {
        String photo = eventDAO.getPhotoPath();
        JsonEventInfo json = JsonEventInfo.from(eventDAO);
        json.setPhoto(photo);
        return json;
    }

    public List<JsonEventInfo> getUpdatedEvent(GetUpdatedEventsRequest getUpdatedEventsRequest, Long id) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        List<JsonEventInfo> result = new ArrayList<>();
//
//        List<EventDAO> eventDAOS = repository.findByIdAndUpdatedAtAfter(id, formatter.parse(getUpdatedEventsRequest.getLastUpdate()));
//        for (EventDAO eventDAO : eventDAOS) {
//            result.add(JsonEventInfo.from(eventDAO));
//        }
//        return result;
        User user = userService.findOneById(id);
        for (EventDAO eventDAO : user.getEventDAOList()) {
            if (eventDAO.getUpdatedAt().after(formatter.parse(getUpdatedEventsRequest.getLastUpdate()))) {
                result.add(JsonEventInfo.from(eventDAO));
            }
        }
        return result;
    }

    public void deleteEvent(int id, User user) throws NoSuchEventPerUserException {
        EventDAO eventDAO = repository.findById(id);
        if (user.getEventDAOList().contains(eventDAO)) {
            repository.removeEventDAOById(id);
        } else {
            throw new NoSuchEventPerUserException(user.getEmail());
        }
    }

    public void createGPS(AddGPSRequest gpsRequest, User user) {
        GpsDAO gps = new GpsDAO();

        gps.setCoordinates(gpsRequest.getCoordinates());
        gps.setEventDAO(repository.findById(Integer.valueOf(gpsRequest.getEventId())));
        gps.setUser(userService.findOneById(user.getId()));

        gpsRepository.save(gps);

    }

}
