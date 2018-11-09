package io.github.joaovicente.piggybank.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.joaovicente.piggybank.dto.CreateKidRequestDto;
import io.github.joaovicente.piggybank.dto.GetKidResponseDto;
import io.github.joaovicente.piggybank.dto.IdResponseDto;
import io.github.joaovicente.piggybank.service.EntityNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
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
        final CreateKidRequestDto kid = CreateKidRequestDto.builder()
                .name(NAME)
                .build();

        final IdResponseDto rspDto = IdResponseDto.builder()
                .id(ID)
                .build();

        given(this.kidService.createKid(kid)).willReturn(rspDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String kidAsJson = objectMapper.writeValueAsString(kid);

        this.mvc.perform(post("/kids").contentType(MediaType.APPLICATION_JSON)
                .content(kidAsJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ID));
    }

    //TODO: Create kid without supplying name should return HTTP status 4xx

    @Test
    public void getKid() throws Exception {
        final String ID = "1";
        final String NAME = "Albert";
        final GetKidResponseDto kid = GetKidResponseDto.builder()
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

        given(this.kidService.getKidById(ID)).willThrow(new EntityNotFoundException());

        this.mvc.perform(get("/kids/" + ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Supplied kid id was not found"));
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

        doThrow(EntityNotFoundException.class).when(kidService).deleteKidById(ID);

        this.mvc.perform(delete("/kids/" + ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Supplied kid id was not found"));
    }

}
