package pl.sdacademy.eventaggregation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
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

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    // region DECLARATIONS
    private static final Long FIRST_INDEX = 1L;
    private static final Long SECOND_INDEX = 2L;
    private static final Event CORRECT_EVENT = Event.builder()
            .address("Address")
            .description("Description")
            .hostUsername("Host")
            .title("Title")
            .idx(FIRST_INDEX)
            .from(LocalDateTime.of(2020, 8, 5, 16, 30))
            .to(LocalDateTime.of(2020, 8, 5, 21, 30))
            .build();
    private static final Event CORRECT_EVENT_WITHOUT_INDEX = Event.builder()
            .address("Address")
            .description("Description")
            .hostUsername("Host")
            .title("Title")
            .from(LocalDateTime.of(2020, 8, 5, 16, 30))
            .to(LocalDateTime.of(2020, 8, 5, 21, 30))
            .build();
    private static final Event ANOTHER_CORRECT_EVENT = Event.builder()
            .address("Sserdda")
            .description("Noitpircsed")
            .hostUsername("Tsoh")
            .title("Eltit")
            .idx(SECOND_INDEX)
            .from(LocalDateTime.of(2020, 8, 5, 16, 30))
            .to(LocalDateTime.of(2020, 8, 5, 21, 30))
            .build();
    private static final EventModel CORRECT_EVENT_MODEL = EventModel.builder()
            .address("Address")
            .description("Description")
            .hostUsername("Host")
            .title("Title")
            .idx(FIRST_INDEX)
            .from(LocalDateTime.of(2020, 8, 5, 16, 30))
            .to(LocalDateTime.of(2020, 8, 5, 21, 30))
            .build();
    private static final EventModel ANOTHER_CORRECT_EVENT_MODEL = EventModel.builder()
            .address("Sserdda")
            .description("Noitpircsed")
            .hostUsername("Tsoh")
            .title("Eltit")
            .idx(SECOND_INDEX)
            .from(LocalDateTime.of(2020, 8, 5, 16, 30))
            .to(LocalDateTime.of(2020, 8, 5, 21, 30))
            .build();
    private static final EventModel CORRECT_EVENT_MODEL_WITHOUT_INDEX = EventModel.builder()
            .address("Address")
            .description("Description")
            .hostUsername("Host")
            .title("Title")
            .from(LocalDateTime.of(2020, 8, 5, 16, 30))
            .to(LocalDateTime.of(2020, 8, 5, 21, 30))
            .build();
    private static final EventModel CORRECT_EVENT_MODEL_CHANGED_WITHOUT_INDEX = EventModel.builder()
            .address("AnotherAddress")
            .description("AnotherDescription")
            .hostUsername("Host")
            .title("AnotherTitle")
            .from(LocalDateTime.of(2020, 8, 5, 16, 30))
            .to(LocalDateTime.of(2020, 8, 5, 21, 30))
            .build();
    private static final EventModel WRONG_EVENT_MODEL_CHANGED_WITHOUT_INDEX = EventModel.builder()
            .address("AnotherAddress")
            .description("AnotherDescription")
            .hostUsername("OtherHost")
            .title("AnotherTitle")
            .from(LocalDateTime.of(2020, 8, 5, 16, 30))
            .to(LocalDateTime.of(2020, 8, 5, 21, 30))
            .build();
    private static final EventModels EVENT_MODELS_WITH_CORRECT_EVENT_MODEL = new EventModels(List.of(CORRECT_EVENT_MODEL));
    private static final EventModels EVENT_MODELS_WITH_ANOTHER_CORRECT_EVENT_MODEL = new EventModels(List.of(ANOTHER_CORRECT_EVENT_MODEL));
    private static final EventModels EVENT_MODELS_WITH_TWO_CORRECT_EVENT_MODELS = new EventModels(List.of(CORRECT_EVENT_MODEL, ANOTHER_CORRECT_EVENT_MODEL));
    //endregion

    @Mock
    private EventConverter eventConverter;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Captor
    private ArgumentCaptor<Event> eventCaptor;

    @Test
    void shouldReturnEventModelsWithValues() {
        when(eventRepository.findAll()).thenReturn(List.of(CORRECT_EVENT));
        when(eventConverter.eventToEventModel(any(Event.class))).thenReturn(CORRECT_EVENT_MODEL);

        final EventModels actualResult = eventService.getAll();

        assertThat(actualResult).isNotNull().isExactlyInstanceOf(EventModels.class).isEqualTo(EVENT_MODELS_WITH_CORRECT_EVENT_MODEL);
        assertThat(actualResult.getEventModels()).isNotNull().hasSize(1).first().isEqualTo(CORRECT_EVENT_MODEL);
    }

    @Test
    void shouldReturnEventModelsWithEmptyList() {
        when(eventRepository.findAll()).thenReturn(List.of());

        final EventModels actualResult = eventService.getAll();

        assertThat(actualResult).isNotNull().isExactlyInstanceOf(EventModels.class);
        assertThat(actualResult.getEventModels()).isEmpty();
    }

    @Test
    void shouldReturnEventModelWithIdx() {
        when(eventRepository.findById(FIRST_INDEX)).thenReturn(Optional.of(CORRECT_EVENT));
        when(eventConverter.eventToEventModel(CORRECT_EVENT)).thenReturn(CORRECT_EVENT_MODEL);

        final EventModel actualResult = eventService.getByIdx(FIRST_INDEX);

        assertThat(actualResult).isNotNull().isExactlyInstanceOf(EventModel.class).isEqualTo(CORRECT_EVENT_MODEL);
    }

    @Test
    void shouldThrowEventExceptionWhenThereIsNoSuchEvent() {
        when(eventRepository.findById(any())).thenReturn(Optional.empty());

        final EventException actualException = assertThrows(EventException.class, () -> eventService.getByIdx(FIRST_INDEX));
        assertThat(actualException).hasMessage("Event with idx " + FIRST_INDEX + " does not exist");
    }

    @Test
    void shouldCreateNewEvent() {
        when(eventRepository.save(CORRECT_EVENT_WITHOUT_INDEX)).thenReturn(CORRECT_EVENT);
        when(eventRepository.findAllByTitle(any(String.class))).thenReturn(List.of());
        when(eventConverter.eventModelToEvent(CORRECT_EVENT_MODEL_WITHOUT_INDEX)).thenReturn(CORRECT_EVENT_WITHOUT_INDEX);

        final EventModel actualResult = eventService.create(CORRECT_EVENT_MODEL_WITHOUT_INDEX);

        assertThat(actualResult).isNotNull().isExactlyInstanceOf(EventModel.class).isEqualTo(CORRECT_EVENT_MODEL);
    }

    @Test
    void shouldThrowEventExceptionWhenCreatingEventWithSameTitleAndDate() {
        when(eventRepository.findAllByTitle(any(String.class))).thenReturn(List.of(CORRECT_EVENT));

        final EventException eventException = assertThrows(EventException.class, () -> eventService.create(CORRECT_EVENT_MODEL_WITHOUT_INDEX));
        assertThat(eventException).hasMessage("You have created same event in this time gap.");
    }

    @Test
    void shouldReturnEventModelsWithValuesOfTitle() {
        when(eventRepository.findAllByTitle(any(String.class))).thenReturn(List.of(CORRECT_EVENT));
        when(eventConverter.eventToEventModel(CORRECT_EVENT)).thenReturn(CORRECT_EVENT_MODEL);

        final EventModels actualResult = eventService.getAllByTitle(CORRECT_EVENT.getTitle());

        assertThat(actualResult).isNotNull().isExactlyInstanceOf(EventModels.class).isEqualTo(EVENT_MODELS_WITH_CORRECT_EVENT_MODEL);
    }

    @Test
    void shouldDeleteExistingEvent() {
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(CORRECT_EVENT));

        eventService.delete(any(Long.class));
        verify(eventRepository).delete(eventCaptor.capture());

        final Event actualResult = eventCaptor.getValue();
        assertThat(actualResult).isNotNull().isEqualTo(CORRECT_EVENT);
    }

    @Test
    void shouldThrowEventExceptionWhileRemoveNonExistEvent() {
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        final EventException eventException = assertThrows(EventException.class, () -> eventService.delete(any(Long.class)));

        assertThat(eventException).hasMessage("Event with given idx does not exist.");
    }

    @Test
    void shouldReplaceEventWithGiven() {
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(CORRECT_EVENT));
        when(eventRepository.save(any(Event.class))).thenReturn(null);

        eventService.updateEvent(FIRST_INDEX, CORRECT_EVENT_MODEL_CHANGED_WITHOUT_INDEX);
        verify(eventRepository).save(eventCaptor.capture());

        final Event actualResult = eventCaptor.getValue();
        assertThat(actualResult).isNotNull().isEqualTo(CORRECT_EVENT);
    }

    @Test
    void shouldThrowEventExceptionWhenHostUsernameIsIncorrect() {
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(CORRECT_EVENT));

        final EventException eventException = assertThrows(EventException.class, () -> eventService.updateEvent(any(Long.class), WRONG_EVENT_MODEL_CHANGED_WITHOUT_INDEX));

        assertThat(eventException).hasMessage("Cannot update event. Host cannot be changed.");
    }

    @ParameterizedTest(name = "[{index}] [Search test]")
    @CsvFileSource(resources = "/EventSearchSingleTest.csv")
    void shouldReturnSingleEvent(final String title, final String host, final String address) {
        if(nonNull(address)) {
            when(eventRepository.findAllByAddressContains(any(String.class))).thenReturn(List.of(ANOTHER_CORRECT_EVENT));
        }
        if(nonNull(title)) {
            when(eventRepository.findAllByTitleContains(any(String.class))).thenReturn(List.of(ANOTHER_CORRECT_EVENT));
        }
        if(nonNull(host)) {
            when(eventRepository.findAllByHostUsernameContains(any(String.class))).thenReturn(List.of(ANOTHER_CORRECT_EVENT));
        }
        when(eventConverter.eventToEventModel(ANOTHER_CORRECT_EVENT)).thenReturn(ANOTHER_CORRECT_EVENT_MODEL);

        final EventModels actualResult = eventService.searchEvents(title, host, address);

        assertThat(actualResult).isNotNull().isExactlyInstanceOf(EventModels.class).isEqualTo(EVENT_MODELS_WITH_ANOTHER_CORRECT_EVENT_MODEL);
        assertThat(actualResult.getEventModels()).hasSize(1);
    }

    @ParameterizedTest(name = "[{index}] [FilterTest]")
    @CsvFileSource(resources = "/EventSearchFilterTest.csv")
    void shouldFilterByFields(final String title, final String host, final String address) {
        if (nonNull(host) && isNull(title)) {
            when(eventRepository.findAllByHostUsernameContains(any(String.class))).thenReturn(List.of(CORRECT_EVENT, ANOTHER_CORRECT_EVENT));
        }
        if (nonNull(title)) {
            when(eventRepository.findAllByTitleContains(any(String.class))).thenReturn(List.of(CORRECT_EVENT, ANOTHER_CORRECT_EVENT));
        }
        when(eventConverter.eventToEventModel(ANOTHER_CORRECT_EVENT)).thenReturn(ANOTHER_CORRECT_EVENT_MODEL);

        final EventModels actualResult = eventService.searchEvents(title, host, address);

        assertThat(actualResult).isNotNull().isExactlyInstanceOf(EventModels.class).isEqualTo(EVENT_MODELS_WITH_ANOTHER_CORRECT_EVENT_MODEL);
        assertThat(actualResult.getEventModels()).hasSize(1);
    }

    @Test
    void shouldReturnAllEvents() {
        when(eventRepository.findAll()).thenReturn(List.of(CORRECT_EVENT, ANOTHER_CORRECT_EVENT));
        when(eventConverter.eventToEventModel(CORRECT_EVENT)).thenReturn(CORRECT_EVENT_MODEL);
        when(eventConverter.eventToEventModel(ANOTHER_CORRECT_EVENT)).thenReturn(ANOTHER_CORRECT_EVENT_MODEL);

        final EventModels actualResult = eventService.searchEvents(null, null, null);

        assertThat(actualResult).isNotNull().isExactlyInstanceOf(EventModels.class).isEqualTo(EVENT_MODELS_WITH_TWO_CORRECT_EVENT_MODELS);
        assertThat(actualResult.getEventModels()).hasSize(2);
    }
}
