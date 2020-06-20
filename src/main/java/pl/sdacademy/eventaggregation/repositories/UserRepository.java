package pl.sdacademy.eventaggregation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sdacademy.eventaggregation.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}
