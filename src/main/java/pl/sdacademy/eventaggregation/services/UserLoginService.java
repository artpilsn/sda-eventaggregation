package pl.sdacademy.eventaggregation.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sdacademy.eventaggregation.domain.User;
import pl.sdacademy.eventaggregation.domain.UserLogin;

@Service
public class UserLoginService {
    private final PasswordEncoder passwordEncoder;

    public UserLoginService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean checkIfPasswordMatches(User user, UserLogin userLogin){
        return passwordEncoder.matches(userLogin.getPassword(),user.getPassword());
    }
}