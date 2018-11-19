package io.github.joaovicente.piggybank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.joaovicente.piggybank.dto.*;
import io.github.joaovicente.piggybank.entity.Transaction;
import io.github.joaovicente.piggybank.service.KidNotFoundException;
import io.github.joaovicente.piggybank.service.TransactionNotFoundException;
import io.github.joaovicente.piggybank.service.TransactionService;
import io.github.joaovicente.piggybank.type.TransactionKind;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.time.DateUtils.parseDateStrictly;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
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
    private final String KID_ID2  = "k2";
    private final String TRANSACTION_ID1  = "t1";
    private final int AMOUNT_100 = 100;
    private final String AMOUNT_100_STR = Integer.toString(AMOUNT_100);
    private final int AMOUNT_200 = 200;
    private final String AMOUNT_200_STR = Integer.toString(AMOUNT_200);
    private final int AMOUNT_0 = 0;
    private final String AMOUNT_0_STR = Integer.toString(AMOUNT_0);
    final String DATE_PATTERN = "yyyy-MM-dd";
    private final String DATE1_STR = "2017-01-01";
    private Date DATE1;
    private final String DATE2_STR = "2018-02-02";
    private Date DATE2;
    private final String KIND_BAD = "BAD_KIND";
    private final String DESCRIPTION_CREDIT1 = "Gift from grandparents";
    private final String DESCRIPTION_DEBIT1 = "Shopping day";

    @TestConfiguration
    static class ModelMapperConfiguration {
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }

    public TransactionControllerTest() {
        try {
            DATE1 = parseDateStrictly(DATE1_STR, DATE_PATTERN);
            DATE2 = parseDateStrictly(DATE2_STR, DATE_PATTERN);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createTransaction() throws Exception {
        final TransactionDto request = TransactionDto.builder()
                .kidId(KID_ID1)
                .date(DATE1)
                .kind(TransactionKind.CREDIT)
                .amount(AMOUNT_100)
                .description(DESCRIPTION_CREDIT1)
                .build();

        // Given transactionService.createTransaction will return Transaction created
        when(this.transactionService.createTransaction(isA(Transaction.class))).thenAnswer(returnsFirstArg());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestAsJson = objectMapper.writeValueAsString(request);

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(requestAsJson).characterEncoding("utf-8"))
                .andExpect(status().isOk())
                //TODO: check ID is a UUID
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(DATE1_STR))
                .andExpect(MockMvcResultMatchers.jsonPath("$.kind").value(request.getKind().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(Integer.toString(request.getAmount())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(request.getDescription()));

    }

    @Test
    public void createTransactionZeroAmount() throws Exception {
        final TransactionDto request = TransactionDto.builder()
                .kidId(KID_ID1)
                .date(DATE1)
                .kind(TransactionKind.CREDIT)
                .amount(AMOUNT_0)
                .description(DESCRIPTION_CREDIT1)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestAsJson = objectMapper.writeValueAsString(request);

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(requestAsJson).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("amount must be above 0"));
    }

    @Test
    public void createTransactionNullDescription() throws Exception {
        String transaction =
                "{" +
                        "\"kidId\":\"" + KID_ID1 + "\"," +
                        "\"date\":\"" + DATE1_STR + "\"," +
                        "\"kind\":\"" + TransactionKind.CREDIT.toString() + "\"," +
                        "\"amount\":" + AMOUNT_100_STR +
                "}";

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("description must not be null"));
    }

    @Test
    public void createTransactionNullDate() throws Exception {
        String transaction =
                "{" +
                        "\"kidId\":\"" + KID_ID1 + "\"," +
                        "\"kind\":\"" + TransactionKind.CREDIT.toString() + "\"," +
                        "\"amount\":" + AMOUNT_100_STR + "," +
                        "\"description\":\"" + DESCRIPTION_CREDIT1 + "\"" +
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
                        "\"date\":\"" + DATE1_STR + "\"," +
                        "\"kind\":\"" + TransactionKind.CREDIT.toString() + "\"," +
                        "\"amount\":" + AMOUNT_100_STR + "," +
                        "\"description\":\"" + DESCRIPTION_CREDIT1 + "\"" +
                        "}";

        given(this.transactionService.createTransaction(isA(Transaction.class)))
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
                        "\"kind\":\"" + TransactionKind.CREDIT.toString() + "\"," +
                        "\"amount\":" + AMOUNT_0 + "," +
                        "\"description\":\"" + DESCRIPTION_CREDIT1 + "\"" +
                "}";

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("date is invalid"));
    }

    @Test
    public void createTransactionKidIdMissing() throws Exception {
        String transaction =
                "{" +
//                        "\"kidId\":\"" + KID_ID1 + "\"," +
                        "\"date\":\"" + DATE1_STR + "\"," +
                        "\"kind\":\"" + TransactionKind.CREDIT.toString() + "\"," +
                        "\"amount\":" + AMOUNT_100_STR + "," +
                        "\"description\":\"" + DESCRIPTION_CREDIT1 + "\"" +
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
                        "\"date\":\"" + DATE1_STR + "\"," +
                        "\"kind\":\"" + TransactionKind.CREDIT.toString() + "\"," +
                        "\"amount\":" + AMOUNT_100_STR + "," +
                        "\"description\":\"" + DESCRIPTION_CREDIT1 + "\"" +
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
                        "\"date\":\"" + DATE1_STR + "\"," +
                        "\"kind\":\"" + KIND_BAD + "\"," +
                        "\"amount\":" + AMOUNT_100_STR + "," +
                        "\"description\":\"" + DESCRIPTION_CREDIT1 + "\"" +
                        "}";

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("kind is invalid"));
    }

    @Test
    public void createTransactionMissingKind() throws Exception {
        String transaction =
                "{" +
                        "\"kidId\":\"" + KID_ID1 + "\"," +
                        "\"date\":\"" + DATE1_STR + "\"," +
//                        "\"kind\":\"" + TransactionKind.CREDIT.toString() + "\"," +
                        "\"amount\":" + AMOUNT_100_STR + "," +
                        "\"description\":\"" + DESCRIPTION_CREDIT1 + "\"" +
                        "}";

        this.mvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
                .content(transaction).characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("kind must not be null"));
    }

    @Test
    public void deleteTransaction() throws Exception {
        doNothing().when(transactionService).deleteTransaction(isA(String.class));
        this.mvc.perform(delete("/transactions/" + TRANSACTION_ID1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTransactionTransactionNotFound() throws Exception {
        doThrow(TransactionNotFoundException.class).when(transactionService).deleteTransaction(isA(String.class));

        this.mvc.perform(delete("/transactions/" + TRANSACTION_ID1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("transactionId not found"));
    }

    @Test
    public void getTransactionsSingleKid() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(
            Transaction.builder()
                    .date(DATE1)
                    .kind(TransactionKind.CREDIT)
                    .amount(AMOUNT_100)
                    .description(DESCRIPTION_CREDIT1)
                    .kidId(KID_ID1)
                .build());
        transactions.add(
            Transaction.builder()
                    .date(DATE2)
                    .kind(TransactionKind.DEBIT)
                    .amount(AMOUNT_200)
                    .description(DESCRIPTION_DEBIT1)
                    .kidId(KID_ID1)
                    .build());

        // Given the Transaction service will return transactions
        given(this.transactionService.getTransactionsByKidId(KID_ID1)).willReturn(transactions);

        // When GET /transactions?kidId={kidId}
        this.mvc.perform(get("/transactions?kidId=" + KID_ID1).accept(MediaType.APPLICATION_JSON))
        // Then Transactions for kidId are returned
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].date").value(DATE1_STR))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].kind").value(TransactionKind.CREDIT.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].amount").value(AMOUNT_100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].description").value(DESCRIPTION_CREDIT1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].kidId").value(KID_ID1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].date").value(DATE2_STR))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].kind").value(TransactionKind.DEBIT.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].amount").value(AMOUNT_200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].description").value(DESCRIPTION_DEBIT1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].kidId").value(KID_ID1));
    }

    @Test
    public void getTransactionsKidNotFound() throws Exception {
        doThrow(KidNotFoundException.class).when(transactionService).getTransactionsByKidId(isA(String.class));

        this.mvc.perform(get("/transactions?kidId=" + KID_ID1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message[0]").value("kidId not found"));
    }
}
