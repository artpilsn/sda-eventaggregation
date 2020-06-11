package pl.sdacademy.eventaggregation.service;

import org.springframework.stereotype.Service;
import pl.sdacademy.eventaggregation.domain.Event;
import pl.sdacademy.eventaggregation.exception.EventException;
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
        final List<EventModel> eventModels = eventRepository.findAll().stream()
                .filter(Objects::nonNull)
                .map(eventConverter::eventToEventModel)
                .collect(Collectors.toList());
        return new EventModels(eventModels);
    }

    public EventModel getByIdx(final Long idx) {
        return eventConverter.eventToEventModel(getIfExist(idx));
    }

    private Event getIfExist(final Long idx) {
        return eventRepository.findById(idx).orElseThrow(() -> new EventException("Event with idx " + idx + " does not exist"));
    }

    public EventModel create(final EventModel model) {
        if (!isEventExisting(model)) {
            model.setIdx(null);
            final Event generatedEvent = eventRepository.save(eventConverter.eventModelToEvent(model));
            model.setIdx(generatedEvent.getIdx());
            return model;
        } else {
            throw new EventException("You have created same event in this time gap.");
        }
    }

    private Boolean isEventExisting(final EventModel model) {
        final List<Event> existingEvents = eventRepository.findAllByTitle(model.getTitle());
        return existingEvents.stream()
                .anyMatch(event -> event.getHostUsername().equals(model.getHostUsername())
                        && !isNotBetweenDates(model, event));
    }

    private Boolean isNotBetweenDates(final EventModel model, final Event event) {
        return model.getFrom().isBefore(event.getFrom()) && model.getTo().isBefore(event.getFrom())
                || model.getFrom().isAfter(event.getTo()) && model.getTo().isAfter(event.getTo());
    }

    public EventModels getAllByTitle(final String title) {
        final List<Event> existingEvents = eventRepository.findAllByTitle(title);
        final List<EventModel> models = existingEvents.stream()
                .map(eventConverter::eventToEventModel)
                .collect(Collectors.toList());
        return new EventModels(models);
    }
}
