package pl.sdacademy.eventaggregation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.nonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventModel {

    private Long idx;

    @NotBlank(message = "Title cannot contains only white space characters")
    @Length(min = 3, max = 30, message = "Title should have 3 to 30 characters.")
    private String title;

    @Length(min = 20, max = 500, message = "Description should be up to 500 characters long - but not less than 20 characters.")
    private String description;
    private String hostUsername;


    private String fromString;
    private String toString;
    private LocalDateTime from;
    private LocalDateTime to;
    private String address;

    @JsonIgnore
    @AssertTrue(message = "Invalid begin/end of event - both fields are required.")
    private boolean isDateValid() {
        convertStringDateToLocalDateTime();
        return nonNull(from) && nonNull(to)
                && from.isAfter(LocalDateTime.now())
                && to.isAfter(from);
    }

    private void  convertStringDateToLocalDateTime() {
        if (nonNull(fromString)) {
            from = LocalDateTime.parse(fromString, DateTimeFormatter.ISO_DATE_TIME);
        }
        if (nonNull(toString)) {
            to = LocalDateTime.parse(toString, DateTimeFormatter.ISO_DATE_TIME);
        }
    }
}
