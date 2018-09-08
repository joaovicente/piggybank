package io.github.joaovicente.piggybank;

import lombok.extern.java.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Log
public class BalanceControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reset();
    }

    @Test
    public void getBalanceEmpty() throws Exception {
        String url = "http://localhost:" + port + "/balance";
        BalanceResponseDto responseBody = this.restTemplate.getForObject(
                url, BalanceResponseDto.class);

        assertThat(responseBody.getBalance(), notNullValue());
        assertThat(responseBody.getBalance(), is(0));
        log.info("getBalanceEmpty response: " + responseBody.toString());

        // TODO: Find a way to get both Body as Object and Status
        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getBalanceAfterCreditAndDebitPair() throws Exception {
        credit(100);
        debit(50);
        BalanceResponseDto responseBody = this.restTemplate.getForObject(
                "http://localhost:" + port + "/balance",
                BalanceResponseDto.class);
        assertThat(responseBody, notNullValue());
        assertThat(responseBody.getBalance(), is(50));
    }

    @Test
    public void getBalanceAfterCreditAndDebitPairNegative() throws Exception {
        credit(50);
        debit(100);
        BalanceResponseDto responseBody = this.restTemplate.getForObject(
                "http://localhost:" + port + "/balance",
                BalanceResponseDto.class);
        assertThat(responseBody, notNullValue());
        assertThat(responseBody.getBalance(), is(-50));
    }

    @Test
    public void getBalanceAfterCreditAndDebitSeveral() throws Exception {
        credit(100); // 100
        debit(20);  // 80
        credit(100); // 180
        debit(30); // 150
        BalanceResponseDto responseBody = this.restTemplate.getForObject(
                "http://localhost:" + port + "/balance",
                BalanceResponseDto.class);
        assertThat(responseBody, notNullValue());
        assertThat(responseBody.getBalance(), is(150));
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
        this.restTemplate.delete("http://localhost:" + port + "/reset");
    }
}