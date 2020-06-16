package pl.sdacademy.eventaggregation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sdacademy.eventaggregation.domain.Event;
import pl.sdacademy.eventaggregation.exception.EventException;
import pl.sdacademy.eventaggregation.model.EventConverter;
import pl.sdacademy.eventaggregation.model.EventModel;
import pl.sdacademy.eventaggregation.model.EventModels;
import pl.sdacademy.eventaggregation.repository.EventRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final EventConverter eventConverter;

    public EventService(final EventRepository eventRepository, final EventConverter eventConverter) {
        this.eventRepository = eventRepository;
        this.eventConverter = eventConverter;
    }

    public EventModels getAll() {
        final List<EventModel> eventModels = eventRepository.findAll().stream()
                .map(eventConverter::eventToEventModel)
                .collect(Collectors.toUnmodifiableList());
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
        }
        throw new EventException("You have created same event in this time gap.");
    }

    private Boolean isEventExisting(final EventModel model) {
        final List<Event> existingEvents = eventRepository.findAllByTitle(model.getTitle());
        return existingEvents.stream()
                .anyMatch(event -> isHostNameAndDateEventMatch(event, model));
    }

    private Boolean isHostNameAndDateEventMatch(final Event event, final EventModel model) {
        return isHostNameEqual(event, model) && !isNotBetweenDates(event, model);
    }

    private Boolean isHostNameEqual(final Event event, final EventModel model) {
        return event.getHostUsername().equals(model.getHostUsername());
    }

    private Boolean isNotBetweenDates(final Event event, final EventModel model) {
        return model.getFrom().isBefore(event.getFrom()) && model.getTo().isBefore(event.getFrom())
                || model.getFrom().isAfter(event.getTo()) && model.getTo().isAfter(event.getTo());
    }

    public EventModels getAllByTitle(final String title) {
        final List<Event> existingEvents = eventRepository.findAllByTitle(title);
        final List<EventModel> models = existingEvents.stream()
                .map(eventConverter::eventToEventModel)
                .collect(Collectors.toUnmodifiableList());
        return new EventModels(models);
    }

    public void delete(final Long idx) {
        final Optional<Event> optionalEvent = eventRepository.findById(idx);
        if (optionalEvent.isPresent()) {
            eventRepository.delete(optionalEvent.get());
        } else {
            throw new EventException("Event with given idx does not exist.");
        }
    }

    public void updateEvent(final Long idx, final EventModel eventChange) {
        final Event existingEvent = getIfExist(idx);
        if (!existingEvent.getHostUsername().equals(eventChange.getHostUsername())) {
            throw new EventException("Cannot update event. Host cannot be changed.");
        }
        existingEvent.updateFields(eventChange);
        eventRepository.save(existingEvent);
    }

    public EventModels searchEvents(final String title, final String host, final String address) {
        final List<Event> eventList;
        if (nonNull(title)) {
            eventList = eventRepository.findAllByTitleContains(title);
            return new EventModels(filterEvents(eventList, host, address));
        }
        if (nonNull(host)) {
            eventList = eventRepository.findAllByHostUsernameContains(host);
            return new EventModels(filterEvents(eventList, address));
        }
        if (nonNull(address)) {
            eventList = eventRepository.findAllByAddressContains(address);
            return new EventModels(convertToModel(eventList));
        }
        return getAll();
    }

    private List<EventModel> filterEvents(final List<Event> events, final String host, final String address) {
        final List<Event> eventList = events.stream()
                .filter(event -> isNullOrExistsInEventField(host, event.getHostUsername()))
                .filter(event -> isNullOrExistsInEventField(address, event.getAddress()))
                .collect(Collectors.toUnmodifiableList());
        return convertToModel(eventList);
    }

    private Boolean isNullOrExistsInEventField(final String field, final String eventField) {
        return isNull(field) || eventField.contains(field);
    }

    private List<EventModel> convertToModel(final List<Event> events) {
        return events.stream()
                .map(eventConverter::eventToEventModel)
                .collect(Collectors.toUnmodifiableList());
    }

    private List<EventModel> filterEvents(final List<Event> events, final String address) {
        final List<Event> eventList = events.stream()
                .filter(event -> isNullOrExistsInEventField(address, event.getAddress()))
                .collect(Collectors.toUnmodifiableList());
        return convertToModel(eventList);
    }
}
