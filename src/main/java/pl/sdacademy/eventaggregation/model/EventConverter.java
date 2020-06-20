package pl.sdacademy.eventaggregation.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.sdacademy.eventaggregation.domain.Event;

@Mapper
public interface EventConverter {

    @Mapping(target = "host", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "idx", ignore = true, source = "idx")
    Event eventModelToEvent(EventModel eventModel);

    @Mapping(target = "hostUsername", source = "host.username")
    EventModel eventToEventModel(Event event);
}
