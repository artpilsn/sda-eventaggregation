package pl.sdacademy.eventaggregation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.AssertTrue;
import java.time.LocalDateTime;

import static java.util.Objects.nonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "events")
public class Event {

    @Id
    @GeneratedValue
    private Long idx;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    //TODO: relation + User object
    @Column(name = "host")
    private String hostUsername;

    @Column(name = "event_starts")
    private LocalDateTime from;

    @Column(name = "event_ends")
    private LocalDateTime to;

    @Column(name = "address")
    private String address;

    @JsonIgnore
    @AssertTrue(message = "Invalid begin/end of event.")
    private boolean isDateValid() {
        return nonNull(from) && nonNull(to)
                && from.isAfter(LocalDateTime.now())
                && to.isAfter(from);
    }
}
