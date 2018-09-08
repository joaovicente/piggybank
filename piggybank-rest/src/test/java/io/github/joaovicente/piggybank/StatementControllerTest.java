package io.github.joaovicente.piggybank;

import io.github.joaovicente.piggybank.dto.CreateCreditRequestDto;
import io.github.joaovicente.piggybank.dto.CreateDebitRequestDto;
import io.github.joaovicente.piggybank.dto.IdResponseDto;
import io.github.joaovicente.piggybank.dto.StatementDto;
import lombok.extern.java.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Log
public class StatementControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reset();
    }

    @Test
    public void getStatementAfterCreditAndDebitPair() throws Exception {
        credit(100);
        debit(50);
        StatementDto responseBody = this.restTemplate.getForObject(
                "http://localhost:" + port + "/statement",
                StatementDto.class);
        assertThat(responseBody, notNullValue());
        assertThat(responseBody.getTransactions().size(), is(2));
    }

    private void credit(int amount) {
        CreateCreditRequestDto requestDto = CreateCreditRequestDto.builder()
                .amount(amount)
                .description("some credit")
                .build();
        HttpEntity<CreateCreditRequestDto> request = new HttpEntity<>(requestDto);
        this.restTemplate.postForObject(
                "http://localhost:" + port + "/credit",
                request,
                IdResponseDto.class);
    }

    private void debit(int amount) {
        CreateDebitRequestDto requestDto = CreateDebitRequestDto.builder()
                .amount(amount)
                .description("some debit")
                .build();
        HttpEntity<CreateDebitRequestDto> request = new HttpEntity<>(requestDto);
        this.restTemplate.postForObject(
                "http://localhost:" + port + "/debit",
                request,
                IdResponseDto.class);
    }
    private void reset() {
        this.restTemplate.postForLocation("http://localhost:" + port + "/reset", null);
    }
}