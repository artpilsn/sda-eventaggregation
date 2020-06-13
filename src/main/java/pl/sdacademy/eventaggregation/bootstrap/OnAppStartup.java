package pl.sdacademy.eventaggregation.bootstrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import pl.sdacademy.eventaggregation.services.UserService;

public class OnAppStartup implements ApplicationListener<ContextRefreshedEvent> {
    private final UserService userService;

    public OnAppStartup(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {



    }
}
