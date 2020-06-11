package pl.sdacademy.eventaggregation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
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

    @NotNull(message = "Title is required")
    @Length(min = 3, max = 30, message = "Title should have 3 to 30 characters.")
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    @Length(min = 20, max = 500, message = "Description should be up to 500 characters long - but not less than 20 characters.")
    private String description;

    //TODO: relation + User object
    @NotNull(message = "Host cannot be empty.")
    @Column(name = "host")
    private String hostUsername;

    @Column(name = "event_starts")
    private LocalDateTime from;

    @Column(name = "event_ends")
    private LocalDateTime to;

    @Column(name = "address")
    private String address;

    @JsonIgnore
    @AssertTrue(message = "Invalid begin/end of event - both fields are required.")
    private boolean isDateValid() {
        return nonNull(from) && nonNull(to)
                && from.isAfter(LocalDateTime.now())
                && to.isAfter(from);
    }
}
