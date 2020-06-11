package pl.sdacademy.eventaggregation.context;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sdacademy.eventaggregation.repository.EventRepository;
import pl.sdacademy.eventaggregation.service.EventService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ContextTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Test
    void shouldEventServiceBeCreated() {
        assertThat(eventService).isNotNull();
    }

    @Test
    void shouldEventRepositoryBeCreated() {
        assertThat(eventRepository).isNotNull();
    }
}
