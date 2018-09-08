package io.github.joaovicente.piggybank;

import lombok.extern.java.Log;
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
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Log

public class CreditControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createCredit() throws Exception {
        CreateCreditRequestDto requestDto = CreateCreditRequestDto.builder()
                .amount(10)
                .description("some description")
                .build();
        HttpEntity<CreateCreditRequestDto> request = new HttpEntity<>(requestDto);

       IdResponseDto responseBody = this.restTemplate.postForObject(
                "http://localhost:" + port + "/credit",
                request,
                IdResponseDto.class);
        assertThat(responseBody.getId(), notNullValue());
        log.info("createCredit test returned: " + responseBody.toString());
    }
}