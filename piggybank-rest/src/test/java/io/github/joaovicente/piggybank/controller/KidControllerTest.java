package io.github.joaovicente.piggybank.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.joaovicente.piggybank.dto.KidDto;
import io.github.joaovicente.piggybank.entity.Kid;
import io.github.joaovicente.piggybank.service.KidNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import io.github.joaovicente.piggybank.service.KidService;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(KidController.class)
public class KidControllerTest {

    @TestConfiguration
    static class ModelMapperConfiguration {
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }

    @Autowired
    private MockMvc mvc;

    @MockBean
    private KidService kidService;
    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void createKid() throws Exception {
        final String ID = "1";
        final String NAME = "Albert";
        final KidDto kidDto = KidDto.builder()
                .name(NAME)
                .build();

        // Given kidService.createKid will return Kid created
        when(this.kidService.createKid(isA(Kid.class))).thenAnswer(returnsFirstArg());

        ObjectMapper objectMapper = new ObjectMapper();
        String kidAsJson = objectMapper.writeValueAsString(kidDto);

        this.mvc.perform(post("/kids").contentType(MediaType.APPLICATION_JSON)
                .content(kidAsJson).characterEncoding("utf-8"))
                .andExpect(status().isOk())
                //TODO: check ID is UUID
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(NAME));
    }

    @Test
    public void createKidWithEmptyName() throws Exception {
        final String NAME = "";
        final KidDto kid = KidDto.builder()
                .name(NAME)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String kidAsJson = objectMapper.writeValueAsString(kid);

        this.mvc.perform(post("/kids").contentType(MediaType.APPLICATION_JSON)
                .content(kidAsJson).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("name must not be empty"));
    }

    @Test
    public void createKidWithoutName() throws Exception {
        this.mvc.perform(post("/kids").contentType(MediaType.APPLICATION_JSON)
                .content("{}").characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[*]")
                        .value(containsInAnyOrder("name must not be empty","name must not be null")));
    }

    @Test
    public void createKidWithNullName() throws Exception {
        this.mvc.perform(post("/kids").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":null}").characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[*]")
                        .value(containsInAnyOrder("name must not be empty","name must not be null")));
    }

    @Test
    public void getKid() throws Exception {
        final String ID = "1";
        final String NAME = "Albert";
        final Kid kid = Kid.builder()
                .name(NAME)
                .build();

        given(this.kidService.getKidById(ID)).willReturn(kid);

        this.mvc.perform(get("/kids/" + ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(kid.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(NAME));
    }

    @Test
    public void getKidNotFound() throws Exception {
        final String ID = "1";

        given(this.kidService.getKidById(ID)).willThrow(new KidNotFoundException());

        this.mvc.perform(get("/kids/" + ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("kidId not found"));
    }


    @Test
    public void deleteKid() throws Exception {
        final String ID = "1";
        doNothing().when(kidService).deleteKidById(ID);

        this.mvc.perform(delete("/kids/" + ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteKidNotFound() throws Exception {
        final String ID = "1";

        doThrow(KidNotFoundException.class).when(kidService).deleteKidById(ID);

        this.mvc.perform(delete("/kids/" + ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("Supplied kid id was not found"));
    }

}
