package com.edsson.expopromoter.api.controller;

import com.edsson.expopromoter.api.model.json.JsonEventInfo;
import com.edsson.expopromoter.api.operator.ImageOperator;
import com.edsson.expopromoter.api.service.EventService;
import io.swagger.annotations.Api;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

@RestController
@RequestMapping("/event")
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Api(value = "event", description = "Event controller")
public class EventController {

    private final EventService service;
    private final ImageOperator imageOperator;

    @Autowired
    public EventController(EventService eventService,ImageOperator imageOperator) {
        this.service = eventService;
        this.imageOperator=imageOperator;
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/{id}")
    public JsonEventInfo getTicketInfo(@PathVariable("id") int id, HttpResponse response) throws IOException {
        return service.buildWithImage(service.findOneById(id));
    }
}
