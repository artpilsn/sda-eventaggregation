package pl.sdacademy.eventaggregation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sdacademy.eventaggregation.domain.Event;
import pl.sdacademy.eventaggregation.domain.Role;
import pl.sdacademy.eventaggregation.domain.User;
import pl.sdacademy.eventaggregation.exception.EventException;
import pl.sdacademy.eventaggregation.model.EventConverter;
import pl.sdacademy.eventaggregation.model.EventModel;
import pl.sdacademy.eventaggregation.repositories.EventRepository;
import pl.sdacademy.eventaggregation.services.EventCrudService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventCrudServiceTest {

    // region DECLARATIONS
    private static final Long FIRST_INDEX = 1L;
    private static final Event CORRECT_EVENT = Event.builder()
            .address("Address")
            .description("Description")
            .host(any(User.class))
            .title("Title")
            .idx(FIRST_INDEX)
            .from(LocalDateTime.of(2020, 8, 5, 16, 30))
            .to(LocalDateTime.of(2020, 8, 5, 21, 30))
            .build();
    private static final Event CORRECT_EVENT_WITHOUT_INDEX = Event.builder()
            .address("Address")
            .description("Description")
            .host(any(User.class))
            .title("Title")
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
    private static final List<Event> EVENTS_WITH_CORRECT_EVENT = List.of(CORRECT_EVENT);
    private static final User ORGANIZER = User.builder()
            .username("Username")
            .firstName("FirstName")
            .lastName("LastName")
            .email("user@email.com")
            .password("password")
            .role(Role.ORGANIZER)
            .build();
    //endregion

    @Mock
    private EventConverter eventConverter;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventCrudService eventCrudService;

    @Captor
    private ArgumentCaptor<Event> eventCaptor;

    @Test
    void shouldReturnEventsListWithSingleElement() {
        when(eventRepository.findAll()).thenReturn(EVENTS_WITH_CORRECT_EVENT);

        final List<Event> actualResult = eventCrudService.getAll();

        assertThat(actualResult).isNotNull().hasSize(1).first().isEqualTo(CORRECT_EVENT);
    }

    @Test
    void shouldReturnEmptyEventList() {
        when(eventRepository.findAll()).thenReturn(List.of());

        final List<Event> actualResult = eventCrudService.getAll();

        assertThat(actualResult).isNotNull().isEmpty();
    }

    @Test
    void shouldReturnEventWithIdx() {
        when(eventRepository.findById(FIRST_INDEX)).thenReturn(Optional.of(CORRECT_EVENT));

        final Event actualResult = eventCrudService.getByIdx(FIRST_INDEX);

        assertThat(actualResult).isNotNull().isExactlyInstanceOf(Event.class).isEqualTo(CORRECT_EVENT);
    }

    @Test
    void shouldThrowEventExceptionWhenThereIsNoSuchEvent() {
        when(eventRepository.findById(any())).thenReturn(Optional.empty());

        final EventException actualException = assertThrows(EventException.class, () -> eventCrudService.getByIdx(FIRST_INDEX));
        assertThat(actualException).hasMessage("Event with idx " + FIRST_INDEX + " does not exist");
    }

    @Test
    void shouldCreateNewEvent() {
        when(eventRepository.save(CORRECT_EVENT_WITHOUT_INDEX)).thenReturn(CORRECT_EVENT);
        when(eventRepository.findAllByTitle(any(String.class))).thenReturn(List.of());
        when(eventConverter.eventModelToEvent(CORRECT_EVENT_MODEL_WITHOUT_INDEX)).thenReturn(CORRECT_EVENT_WITHOUT_INDEX);

        final Event actualResult = eventCrudService.create(CORRECT_EVENT_MODEL_WITHOUT_INDEX, ORGANIZER);

        assertThat(actualResult).isNotNull().isExactlyInstanceOf(Event.class).isEqualTo(CORRECT_EVENT);
    }

    @Test
    void shouldThrowEventExceptionWhenCreatingEventWithSameTitleAndDate() {
        when(eventRepository.findAllByTitle(any(String.class))).thenReturn(EVENTS_WITH_CORRECT_EVENT);

        final EventException eventException = assertThrows(EventException.class,
                () -> eventCrudService.create(CORRECT_EVENT_MODEL_WITHOUT_INDEX, ORGANIZER));
        assertThat(eventException).hasMessage("You have created same event in this time gap.");
    }

    @Test
    void shouldReturnEventListWithValuesOfTitle() {
        when(eventRepository.findAllByTitle(any(String.class))).thenReturn(EVENTS_WITH_CORRECT_EVENT);

        final List<Event> actualResult = eventCrudService.getAllByTitle(CORRECT_EVENT.getTitle());

        assertThat(actualResult).isNotNull().isEqualTo(EVENTS_WITH_CORRECT_EVENT);
    }

    @Test
    void shouldDeleteExistingEvent() {
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(CORRECT_EVENT));

        eventCrudService.delete(any(Long.class));
        verify(eventRepository).delete(eventCaptor.capture());

        final Event actualResult = eventCaptor.getValue();
        assertThat(actualResult).isNotNull().isEqualTo(CORRECT_EVENT);
    }

    @Test
    void shouldThrowEventExceptionWhileRemoveNonExistEvent() {
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        final EventException eventException = assertThrows(EventException.class, () -> eventCrudService.delete(any(Long.class)));

        assertThat(eventException).hasMessage("Event with given idx does not exist.");
    }

    @Test
    void shouldReplaceEventWithGiven() {
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(CORRECT_EVENT));
        when(eventRepository.save(any(Event.class))).thenReturn(null);

        eventCrudService.updateEvent(FIRST_INDEX, CORRECT_EVENT_MODEL_CHANGED_WITHOUT_INDEX);
        verify(eventRepository).save(eventCaptor.capture());

        final Event actualResult = eventCaptor.getValue();
        assertThat(actualResult).isNotNull().isEqualTo(CORRECT_EVENT);
    }

    @Test
    void shouldThrowEventExceptionWhenHostUsernameIsIncorrect() {
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(CORRECT_EVENT));

        final EventException eventException = assertThrows(EventException.class, () -> eventCrudService.updateEvent(any(Long.class), WRONG_EVENT_MODEL_CHANGED_WITHOUT_INDEX));

        assertThat(eventException).hasMessage("Cannot update event. Host cannot be changed.");
    }
}
