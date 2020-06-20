package pl.sdacademy.eventaggregation.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.sdacademy.eventaggregation.domain.Role;
import pl.sdacademy.eventaggregation.domain.User;
import pl.sdacademy.eventaggregation.model.EventModel;
import pl.sdacademy.eventaggregation.services.EventCrudService;
import pl.sdacademy.eventaggregation.services.UserService;

import java.time.LocalDateTime;

@Component
@Profile("develop")
public class OnAppStartup implements CommandLineRunner {

    private final EventCrudService eventCrudService;
    private final UserService userService;

    private static final EventModel MODEL = EventModel.builder()
            .title("Title")
            .description("Long as hell description that match validation")
            .hostUsername("Arek01")
            .address("Address")
            .from(LocalDateTime.of(2020, 7, 1, 12, 0))
            .to(LocalDateTime.of(2020, 7, 2, 12, 0))
            .build();
    private static final EventModel OTHER_MODEL = EventModel.builder()
            .title("OtherTitle")
            .description("Other long as hell description that match validation")
            .hostUsername("Basia01")
            .address("OtherAddress")
            .from(LocalDateTime.of(2020, 7, 1, 12, 0))
            .to(LocalDateTime.of(2020, 7, 2, 12, 0))
            .build();


    public OnAppStartup(final UserService userService, final EventCrudService eventCrudService) {
        this.userService = userService;
        this.eventCrudService = eventCrudService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        User user1 = new User("Arek","Arekowicz","Arek01","BasiaBasia","arek@test.pl");
        User user2 = new User("Basia","Basiadottir","Basia01","BasiaBasia","basia@test.pl");
        User user3 = new User("Czesiek","Czesiekson","CzesiekC","kormoran","mleczyk@test.pl");
        user1.setRole(Role.NORMAL_USER);
        user2.setRole(Role.PARTICIPATOR);
        user3.setRole(Role.ORGANIZER);
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        eventCrudService.create(MODEL);
        eventCrudService.create(OTHER_MODEL);
    }
}
