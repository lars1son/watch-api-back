package com.edsson.expopromoter.api.service;


import com.edsson.expopromoter.api.exceptions.GpsForThisEventNotFoundException;
import com.edsson.expopromoter.api.exceptions.InternalServerErrorException;
import com.edsson.expopromoter.api.exceptions.NoSuchEventPerUserException;
import com.edsson.expopromoter.api.exceptions.PrivilegiousException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdministratorService {
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
        EventDAO eventDAO = eventService.findOneById(id);
        eventService.deleteEvent(id, eventDAO.getUserCreatorId());
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
        }
        else {
            throw new GpsForThisEventNotFoundException();
        }
    }

}
