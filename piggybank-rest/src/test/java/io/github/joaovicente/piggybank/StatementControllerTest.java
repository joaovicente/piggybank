package io.github.joaovicente.piggybank;

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
    private RestApiUtil api;

    @Before
    public void setup() {
        api = new RestApiUtil(port, restTemplate);
        api.reset();
    }

    @Test
    public void getStatementAfterCreditAndDebitPair() throws Exception {
        api.credit(100);
        api.debit(50);
        StatementDto responseBody = this.restTemplate.getForObject(
                "http://localhost:" + port + "/statement",
                StatementDto.class);
        assertThat(responseBody, notNullValue());
        assertThat(responseBody.getTransactions().size(), is(2));
    }

}