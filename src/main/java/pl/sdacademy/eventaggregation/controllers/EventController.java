package pl.sdacademy.eventaggregation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sdacademy.eventaggregation.services.EventCrudService;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventCrudService eventCrudService;

    public EventController(final EventCrudService eventCrudService) {
        this.eventCrudService = eventCrudService;
    }

    @GetMapping
    public String getAll(final ModelMap modelMap) {
        modelMap.addAttribute("events", eventCrudService.getAll());
        return "events";
    }
}
