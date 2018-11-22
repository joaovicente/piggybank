package io.github.joaovicente.piggybank.controller;

import io.github.joaovicente.piggybank.dto.KidAndBalancesDto;
import io.github.joaovicente.piggybank.service.QueryService;
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

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(QueryController.class)
public class QueryControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    QueryService queryService;

    @Test
    public void getKidsAndBalances() throws Exception {
        final String KID1_ID = "k1";
        final String KID2_ID = "k2";
        final String KID3_ID = "k3";
        final String KID1_NAME = "Albert";
        final String KID2_NAME = "Beth";
        final String KID3_NAME = "Carol";
        final int KID1_BALANCE= 10;
        final int KID2_BALANCE= 20;
        final int KID3_BALANCE= 30;
        List<KidAndBalancesDto> kidAndBalancesDtoList = new ArrayList<>();
        kidAndBalancesDtoList.add(KidAndBalancesDto.builder()
                .kidId(KID1_ID).kidName(KID1_NAME).kidBalance(KID1_BALANCE).build());
        kidAndBalancesDtoList.add(KidAndBalancesDto.builder()
                .kidId(KID2_ID).kidName(KID2_NAME).kidBalance(KID2_BALANCE).build());
        kidAndBalancesDtoList.add(KidAndBalancesDto.builder()
                .kidId(KID3_ID).kidName(KID3_NAME).kidBalance(KID3_BALANCE).build());

        given(queryService.getKidsAndBalance()).willReturn(kidAndBalancesDtoList);


        this.mvc.perform(get("/kids-and-balances").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].kidId").value(KID1_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].kidName").value(KID1_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].kidBalance").value(KID1_BALANCE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].kidId").value(KID2_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].kidName").value(KID2_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].kidBalance").value(KID2_BALANCE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].kidId").value(KID3_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].kidName").value(KID3_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].kidBalance").value(KID3_BALANCE));
    }

}
