package pl.sdacademy.eventaggregation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sdacademy.eventaggregation.model.EventConverter;
import pl.sdacademy.eventaggregation.model.EventModel;
import pl.sdacademy.eventaggregation.model.EventModels;
import pl.sdacademy.eventaggregation.services.EventCrudService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventCrudService eventCrudService;
    private final EventConverter eventConverter;

    public EventController(final EventCrudService eventCrudService, final EventConverter eventConverter) {
        this.eventCrudService = eventCrudService;
        this.eventConverter = eventConverter;
    }

    @GetMapping
    public String getAll(final ModelMap modelMap) {
        modelMap.addAttribute("events", prepareAllEventsToSend());
        return "events";
    }

    private EventModels prepareAllEventsToSend() {
        return new EventModels(convertEventListToEventModelList());
    }

    private List<EventModel> convertEventListToEventModelList() {
        return eventCrudService.getAll().stream()
                .map(eventConverter::eventToEventModel)
                .collect(Collectors.toUnmodifiableList());
    }
}
