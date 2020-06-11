package pl.sdacademy.eventaggregation.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sdacademy.eventaggregation.domain.Event;
import pl.sdacademy.eventaggregation.exception.EventException;
import pl.sdacademy.eventaggregation.model.EventConverter;
import pl.sdacademy.eventaggregation.model.EventModel;
import pl.sdacademy.eventaggregation.model.EventModels;
import pl.sdacademy.eventaggregation.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
                .from(LocalDateTime.of(2020, 8, 5, 16, 30))
                .to(LocalDateTime.of(2020, 8, 5, 21, 30))
                .build();
        final EventModel eventModel = EventModel.builder()
                .address("Weird 64")
                .description("Pogo night")
                .hostUsername("MetalHead")
                .title("Let's Mosh Pit")
                .idx(1L)
                .from(LocalDateTime.of(2020, 8, 5, 16, 30))
                .to(LocalDateTime.of(2020, 8, 5, 21, 30))
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
    void shouldReturnEventModelsWithEmptyList(){
        when(eventRepository.findAll()).thenReturn(List.of());

        final EventModels actualResult = eventService.getAll();

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isExactlyInstanceOf(EventModels.class);
        assertThat(actualResult.getEventModels()).isEmpty();
    }

    @Test
    void shouldReturnEventModelWithIdx() {
        final Long idx = 1L;
        final Event event = Event.builder()
                .idx(idx)
                .title("xxxx")
                .address("xxxxx 32")
                .description("xxxxxx")
                .from(LocalDateTime.of(2020, 8, 5, 16, 30))
                .to(LocalDateTime.of(2020, 8, 5, 21, 30))
                .hostUsername("Enessetere")
                .build();
        final EventModel result = EventModel.builder()
                .idx(idx)
                .title("xxxx")
                .address("xxxxx 32")
                .description("xxxxxx")
                .from(LocalDateTime.of(2020, 8, 5, 16, 30))
                .to(LocalDateTime.of(2020, 8, 5, 21, 30))
                .hostUsername("Enessetere")
                .build();
        when(eventRepository.findById(idx)).thenReturn(Optional.of(event));
        when(eventConverter.eventToEventModel(event)).thenReturn(result);

        final EventModel actualResult = eventService.getById(idx);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isExactlyInstanceOf(EventModel.class);
        assertThat(actualResult).isEqualTo(result);
    }

    @Test
    void shouldThrowEventExceptionWhenThereIsNoSuchEvent() {
        final Long idx = 1L;
        when(eventRepository.findById(any())).thenReturn(Optional.empty());

        final EventException actualException = Assertions.assertThrows(EventException.class, () -> eventService.getById(idx));
        assertThat(actualException).hasMessage("Event with idx " + idx + " does not exist");
    }
}
