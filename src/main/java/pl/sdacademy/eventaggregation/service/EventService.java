package pl.sdacademy.eventaggregation.service;

import org.springframework.stereotype.Service;
import pl.sdacademy.eventaggregation.domain.Event;
import pl.sdacademy.eventaggregation.model.EventConverter;
import pl.sdacademy.eventaggregation.model.EventModel;
import pl.sdacademy.eventaggregation.model.EventModels;
import pl.sdacademy.eventaggregation.repository.EventRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventConverter eventConverter;

    public EventService(final EventRepository eventRepository, final EventConverter eventConverter) {
        this.eventRepository = eventRepository;
        this.eventConverter = eventConverter;
    }

    public EventModels getAll() {
        final List<Event> events = eventRepository.findAll();
        final List<EventModel> eventModels = events.stream()
                .filter(Objects::nonNull)
                .map(eventConverter::eventToEventModel)
                .collect(Collectors.toList());
        return new EventModels(eventModels);
    }
}
