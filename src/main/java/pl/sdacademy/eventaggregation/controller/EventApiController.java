package pl.sdacademy.eventaggregation.controller;

import org.springframework.web.bind.annotation.*;
import pl.sdacademy.eventaggregation.model.EventModel;
import pl.sdacademy.eventaggregation.model.EventModels;
import pl.sdacademy.eventaggregation.service.EventService;

@RestController
@RequestMapping("/api/events")
public class EventApiController {

    private final EventService eventService;

    public EventApiController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public EventModels getAllEvents() {
        return eventService.getAll();
    }

    @GetMapping("/{idx}")
    public EventModel getEventById(@PathVariable final Long idx) {
        return eventService.getByIdx(idx);
    }

    @GetMapping("/search")
    public EventModels searchEvents(@RequestParam(required = false) final String title,
                                    @RequestParam(required = false) final String host,
                                    @RequestParam(required = false) final String address) {
        return eventService.searchEvents(title, host, address);
    }
}
