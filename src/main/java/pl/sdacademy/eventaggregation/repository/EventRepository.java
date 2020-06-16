package pl.sdacademy.eventaggregation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sdacademy.eventaggregation.domain.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByTitle(String title);
    List<Event> findAllByTitleContains(String title);
    List<Event> findAllByHostUsernameContains(String hostUsername);
    List<Event> findAllByAddressContains(String address);
}
