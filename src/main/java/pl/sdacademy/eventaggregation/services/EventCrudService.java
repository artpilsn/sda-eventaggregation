package pl.sdacademy.eventaggregation.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sdacademy.eventaggregation.domain.Event;
import pl.sdacademy.eventaggregation.domain.Role;
import pl.sdacademy.eventaggregation.domain.User;
import pl.sdacademy.eventaggregation.exception.EventException;
import pl.sdacademy.eventaggregation.exception.UserException;
import pl.sdacademy.eventaggregation.model.EventConverter;
import pl.sdacademy.eventaggregation.model.EventModel;
import pl.sdacademy.eventaggregation.repositories.EventRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventCrudService {

    private final EventRepository eventRepository;
    private final EventConverter eventConverter;
    private final UserService userService;

    public EventCrudService(final EventRepository eventRepository,
                            final EventConverter eventConverter,
                            final UserService userService) {
        this.eventRepository = eventRepository;
        this.eventConverter = eventConverter;
        this.userService = userService;
    }

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event getByIdx(final Long idx) {
        return getIfExist(idx);
    }

    private Event getIfExist(final Long idx) {
        return eventRepository.findById(idx).orElseThrow(() -> new EventException("Event with idx " + idx + " does not exist"));
    }

    public Event create(final EventModel model, final User user) {
        if (user.getRole() == Role.ORGANIZER) {
            if (!isEventExisting(model)) {
                model.setIdx(null);
                model.setHostUsername(user.getUsername());
                return eventRepository.save(prepareEventToSave(model));
            }
            throw new EventException("You have created same event in this time gap.");
        }
        throw new UserException("You don't have privilege to create new event.");
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
        return event.getHost().getUsername().equals(model.getHostUsername());
    }

    private Boolean isNotBetweenDates(final Event event, final EventModel model) {
        return model.getFrom().isBefore(event.getFrom()) && model.getTo().isBefore(event.getFrom())
                || model.getFrom().isAfter(event.getTo()) && model.getTo().isAfter(event.getTo());
    }

    private Event prepareEventToSave(final EventModel model) {
        final Event event = eventConverter.eventModelToEvent(model);
        event.setHost(userService.getByUsername(model.getHostUsername()));
        return event;
    }

    public List<Event> getAllByTitle(final String title) {
        return eventRepository.findAllByTitle(title);
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
        if (!existingEvent.getHost().getUsername().equals(eventChange.getHostUsername())) {
            throw new EventException("Cannot update event. Host cannot be changed.");
        }
        existingEvent.updateFields(eventChange);
        eventRepository.save(existingEvent);
    }
}
