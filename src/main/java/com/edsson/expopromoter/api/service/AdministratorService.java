package com.edsson.expopromoter.api.service;


import com.edsson.expopromoter.api.exceptions.*;
import com.edsson.expopromoter.api.model.EventDAO;
import com.edsson.expopromoter.api.model.GpsDAO;
import com.edsson.expopromoter.api.model.User;
import com.edsson.expopromoter.api.model.json.JsonEventInfo;
import com.edsson.expopromoter.api.model.json.JsonJpsPerEvent;
import com.edsson.expopromoter.api.model.json.ListJsonJpsPerEvent;
import com.edsson.expopromoter.api.repository.EventRepository;
import com.edsson.expopromoter.api.repository.GPSRepository;
import com.edsson.expopromoter.api.repository.RoleRepository;
import com.edsson.expopromoter.api.repository.UserRepository;
import com.edsson.expopromoter.api.request.CreateEventRequest;
import javassist.NotFoundException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdministratorService {

    private static final Logger logger = Logger.getLogger(AdministratorService.class);
    private final RoleRepository roleRepository;
    private final EventService eventService;
    private final GPSRepository gpsRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public AdministratorService(EventRepository eventRepository, GPSRepository gpsRepository, UserRepository userRepository, RoleRepository roleRepository, EventService eventService) {
        this.roleRepository = roleRepository;
        this.eventService = eventService;
        this.gpsRepository = gpsRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;

    }

    public void deleteEvent(int id, User user) throws PrivilegiousException, NoSuchEventPerUserException {

        eventService.deleteEvent(id, user);
    }

    public List<JsonEventInfo> getEventList(int page) throws InternalServerErrorException {
        List<EventDAO> listEventDAO = eventService.getListEventByPage(page);

        if (listEventDAO == null) {
            throw new InternalServerErrorException();
        }
        List<JsonEventInfo> listEventInfo = new ArrayList<>();
        for (EventDAO eventDAO : listEventDAO) {
            listEventInfo.add(JsonEventInfo.from(eventDAO));
        }
        return listEventInfo;

    }

    public ListJsonJpsPerEvent buildInfoByJps(Long id) throws GpsForThisEventNotFoundException {
        List<GpsDAO> gpsDAOS = gpsRepository.findAllByEventDAO_Id(Math.toIntExact(id));

        if (gpsDAOS.size() >= 1) {
            List<JsonJpsPerEvent> perEventList = new ArrayList<>();

            for (GpsDAO gpsDAO : gpsDAOS) {
                User user = userRepository.findOneById(Long.valueOf(gpsDAO.getUser().getId()));
                JsonJpsPerEvent jsonJpsPerEvent = new JsonJpsPerEvent(user.getEmail(), user.getFullName(), gpsDAO.getUpdatedAt().toString(), user.getPhoneNumber(), gpsDAO.getLatitude() + " * " + gpsDAO.getLongtitude());
                perEventList.add(jsonJpsPerEvent);
            }
            EventDAO eventDAO = eventRepository.findById(gpsDAOS.get(0).getEventDAO().getId());
            ListJsonJpsPerEvent listJsonJpsPerEvent = new ListJsonJpsPerEvent(eventDAO.getName(), eventDAO.getDateStart().toString(), perEventList);
            return listJsonJpsPerEvent;
        } else {
            throw new GpsForThisEventNotFoundException();
        }
    }


    public void buildEventsFromCsv(MultipartFile multipartFile, User user) throws IOException, InternalServerErrorException, FailedToUploadImageToAWSException, SystemConfigurationException, ParseException, EntityAlreadyExistException, NotFoundException, EventBadCredentialsException {
        File file = convertMultiPartToFile(multipartFile);
        try (
                Reader reader = new FileReader(file);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        ) {
            for (CSVRecord csvRecord : csvParser) {
                // Accessing Values by Column Index
                String name = csvRecord.get(0);
                String ticket_url = csvRecord.get(1);
//                String user_creator_id = csvRecord.get(2);
                String event_website = csvRecord.get(2);
                String event_location = csvRecord.get(3);
                String description = csvRecord.get(4);
                String agenda = csvRecord.get(5);
                String contacts = csvRecord.get(6);
                String date_start = csvRecord.get(7);
                String date_end = csvRecord.get(8);

                eventService.createEventDAO(new CreateEventRequest(date_start, name, date_end, event_location, event_website, description, agenda, contacts, ticket_url), user);
                logger.info("Event " + name + " created by Administrator");
            }
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

}
