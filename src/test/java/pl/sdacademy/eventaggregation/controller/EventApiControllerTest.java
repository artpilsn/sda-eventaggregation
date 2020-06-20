package pl.sdacademy.eventaggregation.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import pl.sdacademy.eventaggregation.controllers.EventApiController;
import pl.sdacademy.eventaggregation.model.EventConverter;
import pl.sdacademy.eventaggregation.model.EventModels;
import pl.sdacademy.eventaggregation.services.EventCrudService;
import pl.sdacademy.eventaggregation.services.EventSearchService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EventApiControllerTest {

    private static final EventModels EMPTY_EVENT_MODELS = new EventModels(List.of());

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private EventCrudService eventCrudService;

    @Mock
    private EventSearchService eventSearchService;

    @Mock
    private EventConverter eventConverter;

    @BeforeEach
    void setup() {
        EventApiController eventApi = new EventApiController(eventCrudService, eventSearchService, eventConverter);
        StandaloneMockMvcBuilder mvcBuilder = MockMvcBuilders.standaloneSetup(eventApi);
        this.mockMvc = mvcBuilder.build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnEventModelsWithEmptyList() throws Exception {
        when(eventCrudService.getAll()).thenReturn(List.of());

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/events")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final EventModels models = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(),
                new TypeReference<>() {
                });
        assertThat(models).isNotNull().isExactlyInstanceOf(EventModels.class).isEqualTo(EMPTY_EVENT_MODELS);
        assertThat(models.getEventModels()).isEmpty();
    }





}
