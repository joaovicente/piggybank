package io.github.joaovicente.piggybank;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.joaovicente.piggybank.dto.CreateCreditRequestDto;
import io.github.joaovicente.piggybank.dto.CreateCreditResponseDto;
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

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Log

public class CreditControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    RestApiUtil api;

    @Before
    public void setup() {
        api = new RestApiUtil(port, restTemplate);
        api.reset();
    }

    @Test
    public void createCredit() throws Exception {
        CreateCreditRequestDto requestDto = CreateCreditRequestDto.builder()
                .amount(10)
                .description("some description")
                .build();
        HttpEntity<CreateCreditRequestDto> request = new HttpEntity<>(requestDto);

       CreateCreditResponseDto responseBody = this.restTemplate.postForObject(
                "http://localhost:" + port + "/credit",
                request,
               CreateCreditResponseDto.class);
        assertThat(responseBody.getId(), notNullValue());
        log.info("createCredit test returned: " + responseBody.toString());
    }
}