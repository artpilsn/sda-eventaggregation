package pl.sdacademy.eventaggregation.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sdacademy.eventaggregation.domain.User;
import pl.sdacademy.eventaggregation.repositories.UserRepository;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(final User user){
        return userRepository.save(user);
    }

    public User getByUsername(final String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }
}


