package io.github.joaovicente.piggybank;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.java.Log;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Log

public class RestApiAcceptanceTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    private RestApiUtil api;

    @Before
    public void setup() {
        api = new RestApiUtil(port, restTemplate);
        api.reset();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = this.port;
    }

    @Test
    public void acceptanceTest()  {
        // Credit
        final String CREDIT_DATE = "2018-01-01";
        final String CREDIT_DESCRIPTION = "my credit";
        final int CREDIT_AMOUNT = 200;
        final String CREDIT = "CREDIT";

        // Debit
        final String DEBIT_DATE = "2018-01-02";
        final String DEBIT_DESCRIPTION = "my debit";
        final int DEBIT_AMOUNT = 50;
        final String DEBIT = "DEBIT";

        // Balance
        final int BALANCE = CREDIT_AMOUNT - DEBIT_AMOUNT;

        // POST /credit 200
        String requestBody =
                "{" +
                        "\"date\":\""+ CREDIT_DATE + "\", " +
                        "\"description\":\"" + CREDIT_DESCRIPTION + "\"," +
                        "\"amount\":" + Integer.toString(CREDIT_AMOUNT) +
                        "}";
        Response responseBody =
                given()
                        .body(requestBody)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("/credit")
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .body("id", isA(String.class))
                        .extract()
                        .response();
        String response = responseBody.asString();
        String creditTransactionId = from(response).getString("id");
        log.info("POST /credit " + "IN: " + requestBody + " OUT: " + responseBody.asString());

        // POST /debit 50
        requestBody =
                "{" +
                        "\"date\":\""+ DEBIT_DATE + "\", " +
                        "\"description\":\"" + DEBIT_DESCRIPTION + "\"," +
                        "\"amount\":" + Integer.toString(DEBIT_AMOUNT) +
                        "}";
        responseBody =
                given()
                        .body(requestBody)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("/debit")
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .body("id", isA(String.class))
                        .extract()
                        .response();
        response = responseBody.asString();
        String debitTransactionId = from(response).getString("id");
        log.info("POST /debit " + "IN: " + requestBody + " OUT: " + responseBody.asString());

        // GET /statement
        responseBody =
                given()
                        .contentType(ContentType.JSON)
                        .when()
                        .get("/statement")
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .body("transactions[1].id", is(debitTransactionId))
                        .body("transactions[1].date", is(DEBIT_DATE))
                        .body("transactions[1].amount", is(DEBIT_AMOUNT))
                        .body("transactions[1].kind", is(DEBIT))
                        .body("transactions[1].description", is(DEBIT_DESCRIPTION))
                        .body("transactions[0].id", is(creditTransactionId))
                        .body("transactions[0].date", is(CREDIT_DATE))
                        .body("transactions[0].amount", is(CREDIT_AMOUNT))
                        .body("transactions[0].kind", is(CREDIT))
                        .body("transactions[0].description", is(CREDIT_DESCRIPTION))
                        .extract()
                        .response();
        log.info("GET /statements OUT: " + responseBody.asString());

        //TODO: Check balance
        //TODO: Test the latest transaction is the top of the list (incorrect behavior currently)
        //TODO: Pull out into acceptance test
    }

    // TODO: Test negative debits return error
    // TODO: Test negative credits return error
}
