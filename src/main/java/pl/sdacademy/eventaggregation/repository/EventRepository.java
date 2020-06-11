package pl.sdacademy.eventaggregation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sdacademy.eventaggregation.domain.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
