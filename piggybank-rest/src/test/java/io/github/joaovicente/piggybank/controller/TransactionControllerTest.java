package io.github.joaovicente.piggybank.controller;


import io.github.joaovicente.piggybank.dto.IdResponseDto;
import io.github.joaovicente.piggybank.dto.TransactionCreateDto;
import io.github.joaovicente.piggybank.dto.TransactionKindDto;
import io.github.joaovicente.piggybank.service.KidNotFoundException;
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
    private final String KIND_BAD = "BAD_KIND";
    private final String CREDIT_DESCRIPTION1 = "Gift from grandparents";

    @Test
    public void createTransaction() throws Exception {
        String transaction =
                "{" +
                    "\"kidId\":\"" + KID_ID1 + "\"," +
                    "\"date\":\"" + DATE1 + "\"," +
                    "\"kind\":\"" + TransactionKindDto.CREDIT.toString() + "\"," +
                    "\"amount\":" + AMOUNT_100 + "," +
                    "\"description\":\"" + CREDIT_DESCRIPTION1 + "\"" +
                "}";

        final IdResponseDto rspDto = IdResponseDto.builder()
                .id(TRANSACTION_ID1)
                .build();

        given(this.transactionService.createTransaction(isA(TransactionCreateDto.class)))
                .willReturn(rspDto);

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction).characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TRANSACTION_ID1));
    }

    @Test
    public void createTransactionZeroAmount() throws Exception {
        String transaction =
                "{" +
                    "\"kidId\":\"" + KID_ID1 + "\"," +
                    "\"date\":\"" + DATE1 + "\"," +
                    "\"kind\":\"" + TransactionKindDto.CREDIT.toString() + "\"," +
                    "\"amount\":" + AMOUNT_0 + "," +
                    "\"description\":\"" + CREDIT_DESCRIPTION1 + "\"" +
                "}";

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("amount must be above 0"));
    }

    @Test
    public void createTransactionNullDescription() throws Exception {
        String transaction =
                "{" +
                        "\"kidId\":\"" + KID_ID1 + "\"," +
                        "\"date\":\"" + DATE1 + "\"," +
                        "\"kind\":\"" + KIND_BAD + "\"," +
                        "\"amount\":" + AMOUNT_100 +
                "}";

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("description must not be null"));
        //TODO: Handle HttpMessageNotReadableException in RestResponseEntityExceptionHandler
    }

    @Test
    public void createTransactionNullDate() throws Exception {
        String transaction =
                "{" +
                        "\"kidId\":\"" + KID_ID1 + "\"," +
                        "\"kind\":\"" + TransactionKindDto.CREDIT.toString() + "\"," +
                        "\"amount\":" + AMOUNT_100 + "," +
                        "\"description\":\"" + CREDIT_DESCRIPTION1 + "\"" +
                 "}";

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("date must not be null"));
    }

    @Test
    public void createTransactionKidNotFound() throws Exception {
        String transaction =
                "{" +
                        "\"kidId\":\"" + KID_ID1 + "\"," +
                        "\"date\":\"" + DATE1 + "\"," +
                        "\"kind\":\"" + TransactionKindDto.CREDIT.toString() + "\"," +
                        "\"amount\":" + AMOUNT_100 + "," +
                        "\"description\":\"" + CREDIT_DESCRIPTION1 + "\"" +
                        "}";

        given(this.transactionService.createTransaction(isA(TransactionCreateDto.class)))
                .willThrow(new KidNotFoundException());

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction).characterEncoding("utf-8"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("kidId not found"));
    }

    @Test
    public void createTransactionBadDate() throws Exception {
        String transaction =
                "{" +
                        "\"kidId\":\"" + KID_ID1 + "\"," +
                        "\"date\":\"" + "12345" + "\"," +
                        "\"kind\":\"" + TransactionKindDto.CREDIT.toString() + "\"," +
                        "\"amount\":" + AMOUNT_0 + "," +
                        "\"description\":\"" + CREDIT_DESCRIPTION1 + "\"" +
                "}";

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("date is invalid"));
        // FIXME: Handle incorrect date
    }

    @Test
    public void createTransactionKidIdMissing() throws Exception {
        String transaction =
                "{" +
//                        "\"kidId\":\"" + KID_ID1 + "\"," +
                        "\"date\":\"" + DATE1 + "\"," +
                        "\"kind\":\"" + TransactionKindDto.CREDIT.toString() + "\"," +
                        "\"amount\":" + AMOUNT_100 + "," +
                        "\"description\":\"" + CREDIT_DESCRIPTION1 + "\"" +
                        "}";

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[*]")
                    .value(containsInAnyOrder("kidId must not be empty","kidId must not be null")));
    }

    @Test
    public void createTransactionKidIdEmpty() throws Exception {
        String transaction =
                "{" +
                        "\"kidId\":\"" + "" + "\"," +
                        "\"date\":\"" + DATE1 + "\"," +
                        "\"kind\":\"" + TransactionKindDto.CREDIT.toString() + "\"," +
                        "\"amount\":" + AMOUNT_100 + "," +
                        "\"description\":\"" + CREDIT_DESCRIPTION1 + "\"" +
                        "}";

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("kidId must not be empty"));
    }

    @Test
    public void createTransactionInvalidKind() throws Exception {
        String transaction =
                "{" +
                        "\"kidId\":\"" + KID_ID1 + "\"," +
                        "\"date\":\"" + DATE1 + "\"," +
                        "\"kind\":\"" + TransactionKindDto.CREDIT.toString() + "\"," +
                        "\"amount\":" + AMOUNT_100 + "," +
                        "\"description\":\"" + CREDIT_DESCRIPTION1 + "\"" +
                        "}";

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("kind is invalid"));
        //TODO: Handle validation of Enum (see TransactionCreateDto @Enum)
    }

    @Test
    public void createTransactionMissingKind() throws Exception {
        String transaction =
                "{" +
                        "\"kidId\":\"" + KID_ID1 + "\"," +
                        "\"date\":\"" + DATE1 + "\"," +
//                        "\"kind\":\"" + TransactionKindDto.CREDIT.toString() + "\"," +
                        "\"amount\":" + AMOUNT_100 + "," +
                        "\"description\":\"" + CREDIT_DESCRIPTION1 + "\"" +
                        "}";

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("kind must not be null"));
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
