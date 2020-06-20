package pl.sdacademy.eventaggregation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.sdacademy.eventaggregation.controllers.EventApiController;
import pl.sdacademy.eventaggregation.model.ErrorMessage;

import java.util.List;

@RestControllerAdvice(assignableTypes = EventApiController.class)
public class EventEndpointExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EventException.class)
    public ErrorMessage handleEventException(final EventException exception) {
        return ErrorMessage.builder()
                .message("Cannot execute this request.")
                .details(List.of(exception.getMessage()))
                .build();
    }

}
