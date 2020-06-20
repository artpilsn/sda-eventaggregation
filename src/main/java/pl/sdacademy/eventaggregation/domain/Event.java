package pl.sdacademy.eventaggregation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import pl.sdacademy.eventaggregation.model.EventModel;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.nonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;

    @NotBlank(message = "Title cannot contains only white space characters")
    @Length(min = 3, max = 30, message = "Title should have 3 to 30 characters.")
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    @Length(min = 20, max = 500, message = "Description should be up to 500 characters long - but not less than 20 characters.")
    private String description;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "host", referencedColumnName = "username")
    @NotNull(message = "Host cannot be empty.")
    private User host;

    @Column(name = "event_starts")
    private LocalDateTime from;

    @Column(name = "event_ends")
    private LocalDateTime to;

    @Column(name = "address")
    private String address;

    @ManyToMany
    private List<User> participants;

    @JsonIgnore
    @AssertTrue(message = "Invalid begin/end of event - both fields are required.")
    private boolean isDateValid() {
        return nonNull(from) && nonNull(to)
                && from.isAfter(LocalDateTime.now())
                && to.isAfter(from);
    }

    public void updateFields(final EventModel model) {
        this.title = model.getTitle();
        this.description = model.getDescription();
        this.address = model.getAddress();
        this.from = model.getFrom();
        this.to = model.getTo();
    }
}
