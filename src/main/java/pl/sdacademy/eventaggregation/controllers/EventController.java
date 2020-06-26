package pl.sdacademy.eventaggregation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import pl.sdacademy.eventaggregation.domain.Role;
import pl.sdacademy.eventaggregation.domain.User;
import pl.sdacademy.eventaggregation.model.EventConverter;
import pl.sdacademy.eventaggregation.model.EventModel;
import pl.sdacademy.eventaggregation.model.EventModels;
import pl.sdacademy.eventaggregation.services.EventCrudService;
import pl.sdacademy.eventaggregation.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventCrudService eventCrudService;
    private final EventConverter eventConverter;
    private final UserService userService;

    public EventController(final EventCrudService eventCrudService, final EventConverter eventConverter, final UserService userService) {
        this.eventCrudService = eventCrudService;
        this.eventConverter = eventConverter;
        this.userService = userService;
    }

    @GetMapping
    public String getAll(final ModelMap modelMap) {
        modelMap.addAttribute("newEvent", new EventModel());
        return "events";
    }

    @ModelAttribute("events")
    private EventModels prepareAllEventsToSend() {
        return new EventModels(convertEventListToEventModelList());
    }

    private List<EventModel> convertEventListToEventModelList() {
        return eventCrudService.getAll().stream()
                .map(eventConverter::eventToEventModel)
                .collect(Collectors.toUnmodifiableList());
    }

    @PostMapping
    public String createNewEvent(@Valid @ModelAttribute final EventModel model, final Errors errors, final Principal principal) {
        final User user = userService.getByUsername(principal.getName());
        if (errors.hasErrors()) {
            return "events";
        }
        eventCrudService.create(model, user);
        return "redirect:/events";
    }
}
