package pl.sdacademy.eventaggregation.bootstrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.sdacademy.eventaggregation.model.EventModel;
import pl.sdacademy.eventaggregation.service.EventService;

import java.time.LocalDateTime;

@Profile("develop")
@Component
public class OnApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    private final EventService eventService;

    private static final EventModel MODEL = EventModel.builder()
            .title("Title")
            .description("Long as hell description that match validation")
            .hostUsername("Host")
            .address("Address")
            .from(LocalDateTime.of(2020, 7, 1, 12, 0))
            .to(LocalDateTime.of(2020, 7, 2, 12, 0))
            .build();

    public OnApplicationStartup(final EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        eventService.create(MODEL);
    }
}
