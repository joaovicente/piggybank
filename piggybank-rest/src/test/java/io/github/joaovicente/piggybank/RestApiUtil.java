package io.github.joaovicente.piggybank;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;

public class RestApiUtil {
    private int port;
    private TestRestTemplate restTemplate;

    public RestApiUtil(int port, TestRestTemplate restTemplate)    {
        this.restTemplate = restTemplate;
        this.port = port;
    }

//    public void credit(int amount) {
//        CreateCreditRequestDto requestDto = CreateCreditRequestDto.builder()
//                .amount(amount)
//                .description("some credit")
//                .build();
//        HttpEntity<CreateCreditRequestDto> request = new HttpEntity<>(requestDto);
//        this.restTemplate.postForObject(
//                "http://localhost:" + port + "/credit",
//                request,
//                IdResponseDto.class);
//    }
//
//    public void debit(int amount) {
//        CreateDebitRequestDto requestDto = CreateDebitRequestDto.builder()
//                .amount(amount)
//                .description("some debit")
//                .build();
//        HttpEntity<CreateDebitRequestDto> request = new HttpEntity<>(requestDto);
//        this.restTemplate.postForObject(
//                "http://localhost:" + port + "/debit",
//                request,
//                IdResponseDto.class);
//    }
//    public void reset() {
//        this.restTemplate.postForLocation("http://localhost:" + port + "/reset", null);
//    }
}
