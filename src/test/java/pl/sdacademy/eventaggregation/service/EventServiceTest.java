package pl.sdacademy.eventaggregation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sdacademy.eventaggregation.domain.Event;
import pl.sdacademy.eventaggregation.model.EventConverter;
import pl.sdacademy.eventaggregation.model.EventModel;
import pl.sdacademy.eventaggregation.model.EventModels;
import pl.sdacademy.eventaggregation.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    EventConverter eventConverter;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void shouldReturnEventModelsWithValues() {
        final Event event = Event.builder()
                .address("Weird 64")
                .description("Pogo night")
                .hostUsername("MetalHead")
                .title("Let's Mosh Pit")
                .idx(1L)
                .from(LocalDateTime.now().plusHours(3))
                .to(LocalDateTime.now().plusHours(6))
                .build();
        final EventModel eventModel = EventModel.builder()
                .address("Weird 64")
                .description("Pogo night")
                .hostUsername("MetalHead")
                .title("Let's Mosh Pit")
                .idx(1L)
                .from(event.getFrom())
                .to(event.getTo())
                .build();
        final EventModels result = new EventModels(List.of(eventModel));
        when(eventRepository.findAll()).thenReturn(List.of(event));
        when(eventConverter.eventToEventModel(any())).thenReturn(eventModel);

        final EventModels actualResult = eventService.getAll();

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isExactlyInstanceOf(EventModels.class);
        assertThat(actualResult.getEventModels()).isNotNull();
        assertThat(actualResult.getEventModels()).hasSize(1);
        assertThat(actualResult).isEqualTo(result);
        assertThat(actualResult.getEventModels().get(0)).isEqualTo(eventModel);
    }

    @Test
    void shouldReturnEventValuesWithEmptyList(){
        when(eventRepository.findAll()).thenReturn(List.of());

        final EventModels actualResult = eventService.getAll();

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isExactlyInstanceOf(EventModels.class);
        assertThat(actualResult.getEventModels()).isEmpty();
    }

}
