package pl.sdacademy.eventaggregation.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.sdacademy.eventaggregation.domain.Event;

@Mapper
public interface EventConverter {

    @Mapping(target = "idx", ignore = true, source = "idx")
    Event eventModelToEvent(EventModel eventModel);

    EventModel eventToEventModel(Event event);
}
