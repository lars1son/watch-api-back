package com.edsson.expopromoter.api.service;


import com.edsson.expopromoter.api.config.SystemConfigurationKeys;
import com.edsson.expopromoter.api.exceptions.*;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    @Value("${expopromoter.system.configuration.admin.page_size}")
    private String pageSize;

    @Autowired
    public EventService(GPSRepository gpsRepository, AmazonClient amazonClient, EventRepository eventRepository, SystemConfigurationServiceImpl systemConfigurationService, UserService userService, ImageOperator imageOperator) {
        this.repository = eventRepository;

        this.amazonClient = amazonClient;
        this.imageOperator = imageOperator;
        this.gpsRepository = gpsRepository;
        this.userService = userService;
        this.systemConfigurationService = systemConfigurationService;
    }

    public EventDAO findOneById(Integer id) {
        return repository.findById(id);
    }

    public void save(EventDAO eventDAO) {
        repository.save(eventDAO);
    }

    public JsonUrl createEventDAO(CreateEventRequest createEventRequest, User user) throws IOException, SystemConfigurationException, ParseException, EntityAlreadyExistException, EventBadCredentialsException, NotFoundException, FailedToUploadImageToAWSException, InternalServerErrorException {


        List<EventDAO> list = repository.findListByName(createEventRequest.getName());
//        if (list != null) {
//            for (EventDAO eventDAO : list) {
//                if (eventDAO.getUserCreatorId().getId() == user.getId()) {
//                    logger.error("Entity has already exist exception");
//                    throw new EntityAlreadyExistException();
//                }
//            }
//        }


        EventDAO eventDAO = new EventDAO();

        if (createEventRequest.getName() != null && createEventRequest.getDateStart() != null && createEventRequest.getDateEnd() != null) {
            eventDAO.setName(createEventRequest.getName());
            eventDAO.setDateStart(formatter.parse(createEventRequest.getDateStart()));
            eventDAO.setDateEnd(formatter.parse(createEventRequest.getDateEnd()));

        } else throw new EventBadCredentialsException();

        eventDAO.setAgenda(createEventRequest.getAgenda());
        eventDAO.setContacts(createEventRequest.getContacts());
        eventDAO.setDescription(createEventRequest.getDescription());
        eventDAO.setEventLocation(createEventRequest.getLocation());
        eventDAO.setEventWebsite(createEventRequest.getWebsite());
        eventDAO.setTicketUrl(createEventRequest.getTicketUrl());
        eventDAO.setUserCreatorId(user.getId());

        eventDAO.getUsers().add(user);
        user.getEvents().add(eventDAO);

        try {
            userService.save(user);
//            repository.save(eventDAO);

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
        logger.info("New event saved! ");


//        try {
//            list = repository.findListByName(eventDAO.getName());
//            for (EventDAO eventDAO1 : list) {
//                if (eventDAO.getUserCreatorId().getId() == eventDAO1.getUserCreatorId().getId()) ;
//                eventDAO = eventDAO1;
//            }
//        } catch (NonUniqueResultException exception) {
//            logger.error("NonUniqueResultException", exception);
//            throw new EntityAlreadyExistException();
//        }

        eventDAO.setEventInfoUrl(systemConfigurationService.getValueByKey(SystemConfigurationKeys.EventInfoURL.URL) + eventDAO.getId());

        if (createEventRequest.getCoverImageBase64() != null) {
            String name = "cover_photo_" + eventDAO.getId();
            eventDAO.setPhotoPath(imageOperator.saveImage(createEventRequest.getCoverImageBase64(), name));
        }
        if (createEventRequest.getInfoImageBase64() != null) {
            String name = "info_photo_" + eventDAO.getId();
            eventDAO.setInfoPhotoPath(imageOperator.saveImage(createEventRequest.getInfoImageBase64(), name));
        }

        userService.save(user);
        logger.info("New event created: [ " + eventDAO.getName() + ", " + eventDAO.getDateStart() + " ," + eventDAO.getDateEnd() + ", " + eventDAO.getEventInfoUrl() + "]");
        return new JsonUrl(eventDAO.getPhotoPath(), eventDAO.getInfoPhotoPath(), eventDAO.getId(), eventDAO.getEventInfoUrl());
    }


    public JsonUrl update(CreateEventRequest eventDAO, HttpServletRequest request) throws NotFoundException, SystemConfigurationException, IOException, ParseException, FailedToUploadImageToAWSException {

        User user = (User) request.getAttribute("user");
        if (user != null) {

            EventDAO savedEvent = repository.findById(eventDAO.getId());
            if (savedEvent.getUserCreatorId() == user.getId() || user.getRole().getRole().equals("ROLE_ADMIN"))
           {
//                user.deleteRecordFromEventDAOList(savedEvent);

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

                    String name = "/cover_" + eventDAO.getId();
                    savedEvent.setPhotoPath(imageOperator.saveImage(eventDAO.getCoverImageBase64(), name));
                }
                if (eventDAO.getInfoImageBase64() != null) {
                    String name = "/event_" + eventDAO.getId();
                    savedEvent.setPhotoPath(imageOperator.saveImage(eventDAO.getInfoImageBase64(), name));

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
                save(savedEvent);
//                user.addToEventDAOList(savedEvent);
//                userService.save(user);
               logger.info("Event update success!");
                return new JsonUrl(savedEvent.getPhotoPath(), savedEvent.getInfoPhotoPath(), savedEvent.getId(), savedEvent.getEventInfoUrl());
            } else
                throw new NotFoundException("User " + user.getEmail() + " does not have permissions to edit event: '" + savedEvent.getName() + "'!");
        } else throw new NotFoundException("User " + user.getEmail() + " not found!");
    }


    public JsonEventInfo buildWithImage(EventDAO eventDAO) throws IOException {
//        String photo = eventDAO.getPhotoPath();

        JsonEventInfo json = JsonEventInfo.from(eventDAO);
//        json.setPhoto(photo);
        return json;
    }

    public List<JsonEventInfo> getUpdatedEvent(GetUpdatedEventsRequest getUpdatedEventsRequest, Long id) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        List<JsonEventInfo> result = new ArrayList<>();

        User user = userService.findOneById(id);
        for (EventDAO eventDAO : user.getEvents()) {
            if (eventDAO.getUpdatedAt().after(formatter.parse(getUpdatedEventsRequest.getLastUpdate()))) {
                result.add(JsonEventInfo.from(eventDAO));
            }
        }
        return result;
    }

    public void deleteEvent(int id, User user) throws NoSuchEventPerUserException {
        EventDAO eventDAO = repository.findById(id);
        if (eventDAO == null) {
            throw new NoSuchEventPerUserException();
        }
        if (user.getRole().equals("ROLE_ADMIN")) {

            repository.removeEventDAOById(id);

        } else if (user.getEvents().contains(eventDAO)) {
            if (eventDAO.getUserCreatorId() == user.getId()) {
                repository.removeEventDAOById(id);
            }
        } else {
            user.getEvents().remove(eventDAO);
        }


    }

    public void createGPS(AddGPSRequest gpsRequest, User user) {
        GpsDAO gps = new GpsDAO();

        gps.setLatitude(gpsRequest.getLatitude());
        gps.setLongtitude(gpsRequest.getLongtitude());
        gps.setEventDAO(repository.findById(Integer.valueOf(gpsRequest.getEventId())));
        gps.setUser(userService.findOneById(user.getId()));
        gpsRepository.save(gps);

    }

    public List<EventDAO> getListEventByPage(int page) {

        PageRequest request =
                new PageRequest(page, Integer.valueOf(pageSize), Sort.Direction.DESC, "id");
        return repository.getEventsById(request);

    }

    public double pageCount() {
        return (Math.ceil(Double.valueOf(repository.count()) / Double.valueOf(pageSize)));
    }
}
