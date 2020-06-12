package pl.sdacademy.eventaggregation.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    EventConverter eventConverter;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Captor
    private ArgumentCaptor<Event> eventCaptor;

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
                .description("xxxxxx")
                .from(LocalDateTime.of(2020, 8, 5, 16, 30))
                .to(LocalDateTime.of(2020, 8, 5, 21, 30))
                .hostUsername("Enessetere")
                .build();
        when(eventRepository.findById(idx)).thenReturn(Optional.of(event));
        when(eventConverter.eventToEventModel(event)).thenReturn(result);

        final EventModel actualResult = eventService.getByIdx(idx);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isExactlyInstanceOf(EventModel.class);
        assertThat(actualResult).isEqualTo(result);
    }

    @Test
    void shouldThrowEventExceptionWhenThereIsNoSuchEvent() {
        final Long idx = 1L;
        when(eventRepository.findById(any())).thenReturn(Optional.empty());

        final EventException actualException = Assertions.assertThrows(EventException.class, () -> eventService.getByIdx(idx));
        assertThat(actualException).hasMessage("Event with idx " + idx + " does not exist");
    }

    @Test
    void shouldCreateNewEvent() {
        final Long idx = 1L;
        final String hostUsername = "Enessetere";
        final String title = "title";
        final String description = "description";
        final LocalDateTime from = LocalDateTime.of(2020,7,1,14,20);
        final LocalDateTime to = LocalDateTime.of(2020,7,1,18,50);
        final EventModel input = EventModel.builder()
                .hostUsername(hostUsername)
                .title(title)
                .description(description)
                .from(from)
                .to(to)
                .build();
        final Event inputEvent = Event.builder()
                .hostUsername(hostUsername)
                .title(title)
                .description(description)
                .from(from)
                .to(to)
                .build();
        final Event generatedEvent = Event.builder()
                .idx(idx)
                .hostUsername(hostUsername)
                .title(title)
                .description(description)
                .from(from)
                .to(to)
                .build();
        final EventModel expectedResult = EventModel.builder()
                .idx(idx)
                .hostUsername(hostUsername)
                .title(title)
                .description(description)
                .from(from)
                .to(to)
                .build();
        when(eventRepository.save(inputEvent)).thenReturn(generatedEvent);
        when(eventRepository.findAllByTitle(title)).thenReturn(List.of());
        when(eventConverter.eventModelToEvent(input)).thenReturn(inputEvent);

        final EventModel actualResult = eventService.create(input);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isExactlyInstanceOf(EventModel.class);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void shouldThrowEventExceptionWhenCreatingEventWithSameTitleAndSimilarDate() {
        final Long idx = 1L;
        final String hostUsername = "Enessetere";
        final String title = "title";
        final String description = "description";
        final LocalDateTime from = LocalDateTime.of(2020,7,1,14,20);
        final LocalDateTime to = LocalDateTime.of(2020,7,1,18,50);
        final EventModel input = EventModel.builder()
                .hostUsername(hostUsername)
                .title(title)
                .description(description)
                .from(from)
                .to(to)
                .build();
        final Event existingEvent = Event.builder()
                .idx(idx)
                .hostUsername(hostUsername)
                .title(title)
                .description(description)
                .from(from.minusMonths(1))
                .to(to.minusHours(2))
                .build();
        when(eventRepository.findAllByTitle(title)).thenReturn(List.of(existingEvent));

        final EventException eventException = Assertions.assertThrows(EventException.class, () -> eventService.create(input));
        assertThat(eventException).hasMessage("You have created same event in this time gap.");
    }

    @Test
    void shouldReturnEventModelsWithValuesOfTitle() {
        final Long idx = 1L;
        final String hostUsername = "Enessetere";
        final String title = "title";
        final String description = "description";
        final LocalDateTime from = LocalDateTime.of(2020,7,1,14,20);
        final LocalDateTime to = LocalDateTime.of(2020,7,1,18,50);
        final Event existingEvent = Event.builder()
                .idx(idx)
                .hostUsername(hostUsername)
                .title(title)
                .description(description)
                .from(from)
                .to(to)
                .build();
        final EventModel outputModel = EventModel.builder()
                .idx(idx)
                .hostUsername(hostUsername)
                .title(title)
                .description(description)
                .from(from)
                .to(to)
                .build();
        final EventModels expectedResult = new EventModels(List.of(outputModel));
        when(eventRepository.findAllByTitle(title)).thenReturn(List.of(existingEvent));
        when(eventConverter.eventToEventModel(existingEvent)).thenReturn(outputModel);

        final EventModels actualResult = eventService.getAllByTitle(title);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isExactlyInstanceOf(EventModels.class);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void shouldDeleteExistingEvent() {
        final Event event = Event.builder()
                .title("title")
                .description("description")
                .hostUsername("host")
                .from(LocalDateTime.of(2020, 7, 1, 12, 0))
                .to(LocalDateTime.of(2020, 7, 1, 15, 0))
                .build();
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));

        eventService.delete(any());

        verify(eventRepository).delete(eventCaptor.capture());
        assertThat(eventCaptor.getValue()).isNotNull();
        assertThat(eventCaptor.getValue()).isEqualTo(event);
    }

    @Test
    void shouldThrowEventExceptionWhileRemoveNonExistEvent() {
        when(eventRepository.findById(any())).thenReturn(Optional.empty());

        final EventException eventException = Assertions.assertThrows(EventException.class, () -> eventService.delete(any()));

        assertThat(eventException).hasMessage("Event with given idx does not exist.");
    }
}
