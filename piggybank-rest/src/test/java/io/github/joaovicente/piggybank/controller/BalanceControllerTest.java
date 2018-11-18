package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.service.BalanceService;
import io.github.joaovicente.piggybank.service.KidNotFoundException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(BalanceController.class)
public class BalanceControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    BalanceService balanceService;

    @Test
    public void getBalance() throws Exception {
        final String KID1 = "k1";
        final int BALANCE = 80;

        // Given
        given(balanceService.calculateKidBalance(KID1)).willReturn(BALANCE);

        // When
        this.mvc.perform(get("/balance?kidId=" + KID1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(BALANCE));
    }

    @Test
    public void getBalanceKidNotFound() throws Exception {
        final String KID1 = "k1";

        // Given
        given(balanceService.calculateKidBalance(KID1)).willThrow(new KidNotFoundException());

        // When
        this.mvc.perform(get("/balance?kidId=" + KID1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("kidId not found"));
    }
}
