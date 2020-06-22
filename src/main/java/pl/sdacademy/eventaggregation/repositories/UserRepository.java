package pl.sdacademy.eventaggregation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sdacademy.eventaggregation.domain.User;

public interface UserRepository extends JpaRepository<User, String> {
}
