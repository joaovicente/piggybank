package io.github.joaovicente.piggybank.controller;


import io.github.joaovicente.piggybank.dto.IdResponseDto;
import io.github.joaovicente.piggybank.dto.TransactionCreateDto;
import io.github.joaovicente.piggybank.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionService transactionService;
    private final String KID_ID1  = "k1";
    private final String TRANSACTION_ID1  = "t1";
    private final String AMOUNT_100 = "100";
    private final String AMOUNT_0 = "0";
    private final String DATE1 = "2018-11-12";
    private final String CREDIT = "CREDIT";
    private final String CREDIT_DESCRIPTION1 = "Gift from grandparents";

    @Test
    public void createTransaction() throws Exception {
        String transaction =
                "{" +
                    "\"date\":\"" + DATE1 + "\"," +
                    "\"kind\":\"" + CREDIT + "\"," +
                    "\"amount\":" + AMOUNT_100 + "," +
                    "\"description\":\"" + CREDIT_DESCRIPTION1 + "\"" +
                "}";

        final IdResponseDto rspDto = IdResponseDto.builder()
                .id(TRANSACTION_ID1)
                .build();

        given(this.transactionService.createTransaction( isA(String.class), isA(TransactionCreateDto.class)))
                .willReturn(rspDto);

        this.mvc.perform(post("/kids/" + KID_ID1 + "/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TRANSACTION_ID1));
    }


    @Test
    public void createTransactionZeroAmount() throws Exception {
        String transaction =
                "{" +
                    "\"date\":\"" + DATE1 + "\"," +
                    "\"kind\":\"" + CREDIT + "\"," +
                    "\"amount\":" + AMOUNT_0 + "," +
                    "\"description\":\"" + CREDIT_DESCRIPTION1 + "\"" +
                "}";

        this.mvc.perform(post("/kids/" + KID_ID1 + "/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("amount must be above 0"));
    }

    @Test
    public void createTransactionNullDescription() throws Exception {
        String transaction =
                "{" +
                        "\"date\":\"" + DATE1 + "\"," +
                        "\"kind\":\"" + CREDIT + "\"," +
                        "\"amount\":" + AMOUNT_100 +
                "}";

        this.mvc.perform(post("/kids/" + KID_ID1 + "/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("description must not be null"));
    }

    @Test
    public void createTransactionNullDate() throws Exception {
        String transaction =
                "{" +
                        "\"kind\":\"" + CREDIT + "\"," +
                        "\"amount\":" + AMOUNT_100 + "," +
                        "\"description\":\"" + CREDIT_DESCRIPTION1 + "\"" +
                 "}";

        this.mvc.perform(post("/kids/" + KID_ID1 + "/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("date must not be null"));
    }

    @Test
    public void createTransactionBadDate() throws Exception {
        fail();
    }

    @Test
    public void createTransactionKidNotFound() throws Exception {
        fail();
    }

    @Test
    public void getTransactions() throws Exception {
        fail();
    }

    @Test
    public void getTransactionsKidNotFound() throws Exception {
        fail();
    }

    @Test
    public void deleteTransaction() throws Exception {
        fail();
    }

    @Test
    public void deleteTransactionKidNotFound() throws Exception {
        fail();
    }
}
