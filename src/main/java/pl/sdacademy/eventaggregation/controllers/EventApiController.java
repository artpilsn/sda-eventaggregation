package pl.sdacademy.eventaggregation.controllers;

import org.springframework.web.bind.annotation.*;
import pl.sdacademy.eventaggregation.domain.Event;
import pl.sdacademy.eventaggregation.model.EventConverter;
import pl.sdacademy.eventaggregation.model.EventModel;
import pl.sdacademy.eventaggregation.model.EventModels;
import pl.sdacademy.eventaggregation.services.EventCrudService;
import pl.sdacademy.eventaggregation.services.EventSearchService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventApiController {

    private final EventCrudService eventCrudService;
    private final EventSearchService eventSearchService;
    private final EventConverter eventConverter;

    public EventApiController(final EventCrudService eventCrudService,
                              final EventSearchService eventSearchService,
                              final EventConverter eventConverter) {
        this.eventCrudService = eventCrudService;
        this.eventSearchService = eventSearchService;
        this.eventConverter = eventConverter;
    }

    @GetMapping
    public EventModels getAllEvents() {
        return new EventModels(convertToEventModelList(eventCrudService.getAll()));
    }

    private List<EventModel> convertToEventModelList(final List<Event> events) {
        return events.stream()
                .map(eventConverter::eventToEventModel)
                .collect(Collectors.toUnmodifiableList());
    }

    @GetMapping("/{idx}")
    public EventModel getEventById(@PathVariable final Long idx) {
        return eventConverter.eventToEventModel(eventCrudService.getByIdx(idx));
    }

    @GetMapping("/search")
    public EventModels searchEvents(@RequestParam(required = false) final String title,
                                    @RequestParam(required = false) final String host,
                                    @RequestParam(required = false) final String address) {
        return new EventModels(convertToEventModelList(eventSearchService.searchEvents(title, host, address)));
    }
}
