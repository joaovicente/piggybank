package io.github.joaovicente.piggybank.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.joaovicente.piggybank.dto.KidCreateDto;
import io.github.joaovicente.piggybank.dto.KidReadDto;
import io.github.joaovicente.piggybank.dto.IdResponseDto;
import io.github.joaovicente.piggybank.service.KidNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import io.github.joaovicente.piggybank.service.KidService;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(KidController.class)
public class KidControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private KidService kidService;

    @Test
    public void createKid() throws Exception {
        final String ID = "1";
        final String NAME = "Albert";
        final KidCreateDto kid = KidCreateDto.builder()
                .name(NAME)
                .build();

        final IdResponseDto rspDto = IdResponseDto.builder()
                .id(ID)
                .build();

        given(this.kidService.createKid(kid)).willReturn(rspDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String kidAsJson = objectMapper.writeValueAsString(kid);

        this.mvc.perform(post("/kids").contentType(MediaType.APPLICATION_JSON)
                .content(kidAsJson).characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID));
    }

    @Test
    public void createKidWithEmptyName() throws Exception {
        final String NAME = "";
        final KidCreateDto kid = KidCreateDto.builder()
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
        final KidReadDto kid = KidReadDto.builder()
                .id(ID)
                .name(NAME)
                .build();

        given(this.kidService.getKidById(ID)).willReturn(kid);

        this.mvc.perform(get("/kids/" + ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID))
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
