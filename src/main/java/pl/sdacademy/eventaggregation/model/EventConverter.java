package pl.sdacademy.eventaggregation.model;

import org.mapstruct.Mapper;
import pl.sdacademy.eventaggregation.domain.Event;

@Mapper
public interface EventConverter {

    Event eventModelToEvent(EventModel eventModel);

    EventModel eventToEventModel(Event event);
}
