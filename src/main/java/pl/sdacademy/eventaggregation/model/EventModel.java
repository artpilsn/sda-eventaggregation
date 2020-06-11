package pl.sdacademy.eventaggregation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventModel {

    private Long idx;
    private String title;
    private String description;
    private String hostUsername;
    private LocalDateTime from;
    private LocalDateTime to;
    private String address;
}
