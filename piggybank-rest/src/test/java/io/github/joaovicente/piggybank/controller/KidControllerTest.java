package io.github.joaovicente.piggybank.controller;


import io.github.joaovicente.piggybank.dto.GetKidResponseDto;
import io.github.joaovicente.piggybank.service.EntityNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.joaovicente.piggybank.model.Kid;
import io.github.joaovicente.piggybank.service.KidService;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

@RunWith(SpringRunner.class)
@WebMvcTest(KidController.class)
public class KidControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private KidService kidService;


//    @Test
//    public void postKidReturnId() throws Exception {
//        String content = "[{\"username\": \"user\", \"accountNumber\": \"123456789\"}]";
//
//        given(this.kidService.getKidById().getUserAccounts()).willReturn(
//                Collections.singletonList(new Account("user", "123456789")));
//
//        this.mvc.perform(get("/kids").accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()).andExpect(content().json(content));
//    }


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

}
