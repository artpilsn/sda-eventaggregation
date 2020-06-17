package pl.sdacademy.eventaggregation.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import pl.sdacademy.eventaggregation.domain.User;
import pl.sdacademy.eventaggregation.repositories.UserRepository;

@Component
public class OnAppStartup implements CommandLineRunner {
    private final UserRepository userRepository;

    public OnAppStartup(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User("Arek","Arekowicz","Arek01","BasiaBasia","arek@test.pl");
        User user2 = new User("Basia","Basiadottir","Basia01","BasiaBasia","basia@test.pl");
        User user3 = new User("Czesiek","Czesiekson","CzesiekC","kormoran","mleczyk@test.pl");
        userRepository.create(user1);
        userRepository.create(user2);
        userRepository.create(user3);
    }
}