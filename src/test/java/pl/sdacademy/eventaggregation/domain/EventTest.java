package pl.sdacademy.eventaggregation.domain;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldValidateEvent() {
        final Event event = Event.builder()
                .title("Some Title")
                .description("Some long description, that will have over 20 characters.")
                .from(LocalDateTime.of(2020,6, 30, 18,0))
                .to(LocalDateTime.of(2020, 7, 1, 3, 30))
                .hostUsername("Enessetere")
                .build();

        validate(event);
    }



    private void validate(final Event event) {
        shouldReturnNoErrors(validator.validate(event));
    }

    private void shouldReturnNoErrors(final Set<ConstraintViolation<Event>> validationResult) {
        assertThat(validationResult).hasSize(0);
    }

    @ParameterizedTest(name = "[FIELDS VALIDATION][{index}]")
    @CsvFileSource(resources = "/EventValidationTest.csv")
    void shouldNotValidate(final String title, final String description, final String hostUsername, final String expectedMessage, final String expectedField) {
        final Event event = Event.builder()
                .title(title)
                .description(description)
                .hostUsername(hostUsername)
                .from(LocalDateTime.of(2020,6, 30, 18,0))
                .to(LocalDateTime.of(2020, 7, 1, 3, 30))
                .build();

        validate(event, expectedMessage, expectedField);
    }

    private void validate(final Event event, final String expectedMessage, final String expectedField) {
        shouldReturnSingleError(validator.validate(event), expectedMessage, expectedField);
    }

    private void shouldReturnSingleError(final Set<ConstraintViolation<Event>> validationResult,
                                               final String expectedMessage, final String expectedField) {
        assertThat(validationResult).hasSize(1);
        assertThat(validationResult.stream()).anyMatch(violation -> violation.getMessage().equals(expectedMessage)
                && ((PathImpl)violation.getPropertyPath()).getLeafNode().getName().equals(expectedField));
    }

    @ParameterizedTest(name = "[EVENT DATE VALIDATION][{index}]")
    @CsvFileSource(resources = "/EventDateValidationTest.csv")
    void shouldFailInDateFields(final Integer fromYear, final Integer fromMonth, final Integer fromDay, final Integer fromHour, final Integer fromMinute,
                                final Integer toYear, final Integer toMonth, final Integer toDay, final Integer toHour, final Integer toMinute,
                                final String expectedMessage, final String expectedField) {
        final Event event = Event.builder()
                .hostUsername("Enessetere")
                .description("Some long text with more than 20 character")
                .title("title")
                .from((fromYear != null) ? LocalDateTime.of(fromYear, fromMonth, fromDay, fromHour, fromMinute) : null)
                .to((toYear != null) ? LocalDateTime.of(toYear, toMonth, toDay, toHour, toMinute) : null)
                .build();

        validate(event, expectedMessage, expectedField);
    }
}
