package pl.sdacademy.eventaggregation.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sdacademy.eventaggregation.domain.Role;
import pl.sdacademy.eventaggregation.domain.User;
import pl.sdacademy.eventaggregation.repositories.UserRepository;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(final User user) {
        final User newUser = new User(user.getFirstName(), user.getLastName(), user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getEmail());
        newUser.setRole(Role.NORMAL_USER);
        userRepository.save(newUser);
    }

    public User getByUsername(final String username){
        return userRepository.findById(username).orElseThrow();
    }

}


