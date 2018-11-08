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

import java.text.SimpleDateFormat;
import java.util.Date;

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
    public void whenICreditAndDebit_thenIGetCorrectBalanceAndStatement()  {
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

        // GET /balance
        responseBody =
                given()
                        .contentType(ContentType.JSON)
                .when()
                        .get("/balance")
                .then()
                        .statusCode(HttpStatus.SC_OK)
                        .body("balance", is(BALANCE))
                        .extract()
                        .response();
        log.info("GET /balance OUT: " + responseBody.asString());
        //TODO: Test the latest transaction is the top of the list (incorrect behavior currently)
    }


    @Test
    public void whenIPostNegativeCredit_thenIgetAnError() {
        final int NEGATIVE_AMOUNT = -1;

        // POST /credit -1
        String requestBody =
                "{" +
                        "\"amount\":" + Integer.toString(NEGATIVE_AMOUNT) +
                "}";
        Response responseBody =
                given()
                        .body(requestBody)
                        .contentType(ContentType.JSON)
                .when()
                        .post("/credit")
                .then()
                        .statusCode(HttpStatus.SC_BAD_REQUEST)
                        .body("error", is("INVALID_CREDIT_AMOUNT"))
                        .body("message", is("Negative values are not allowed"))
                        .extract()
                        .response();
        log.info("POST /credit " + "IN: " + requestBody + " OUT: " + responseBody.asString());
    }

    @Test
    public void whenIPostNegativeDebit_thenIgetAnError() {
        final int NEGATIVE_AMOUNT = -1;

        // POST /debit 50
        String requestBody =
                "{" +
                        "\"amount\":" + Integer.toString(NEGATIVE_AMOUNT) +
                "}";
        Response responseBody =
                given()
                        .body(requestBody)
                        .contentType(ContentType.JSON)
                .when()
                        .post("/debit")
                .then()
                        .statusCode(HttpStatus.SC_BAD_REQUEST)
                        .body("error", is("INVALID_DEBIT_AMOUNT"))
                        .body("message", is("Negative values are not allowed"))
                        .extract()
                        .response();
        log.info("POST /credit " + "IN: " + requestBody + " OUT: " + responseBody.asString());
    }


    @Test
    public void whenIPostCreditWithoutDate_thenIgetTodaysDate() {
        final int AMOUNT = 50;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String todaysDate = dateFormat.format(date);

        // POST /credit 50 without date
        String requestBody =
                "{" +
                        "\"amount\":" + Integer.toString(AMOUNT) +
                "}";
        Response responseBody =
                given()
                        .body(requestBody)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("/credit")
                .then()
                        .statusCode(HttpStatus.SC_OK)
                        .body("date", is(todaysDate))
                        .extract()
                        .response();
        log.info("POST /credit " + "IN: " + requestBody + " OUT: " + responseBody.asString());
    }

//    @Test
//    public void whenIPostCreditBadDate_thenIgetAnError() {
//        final int AMOUNT = 50;
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date();
//        String todaysDate = dateFormat.format(date);
//
//        // POST /credit 50 without date
//        String requestBody =
//                "{" +
//                        "\"amount\":" + Integer.toString(AMOUNT) +
//                        "\"date\":" + "abcd" +
//
//                "}";
//        Response responseBody =
//                given()
//                        .body(requestBody)
//                        .contentType(ContentType.JSON)
//                .when()
//                        .post("/credit")
//                .then()
//                        .statusCode(HttpStatus.SC_BAD_REQUEST)
//                        .body("error", isA(String.class))
//                        .extract()
//                        .response();
//        log.info("POST /credit " + "IN: " + requestBody + " OUT: " + responseBody.asString());
//    }

    @Test
    public void whenIPostDebitWithoutDate_thenIgetTodaysDate() {
        final int AMOUNT = 50;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String todaysDate = dateFormat.format(date);

        // POST /credit 50 without date
        String requestBody =
                "{" +
                        "\"amount\":" + Integer.toString(AMOUNT) +
                "}";
        Response responseBody =
                given()
                        .body(requestBody)
                        .contentType(ContentType.JSON)
                .when()
                        .post("/debit")
                .then()
                        .statusCode(HttpStatus.SC_OK)
                        .body("date", is(todaysDate))
                        .extract()
                        .response();
        log.info("POST /credit " + "IN: " + requestBody + " OUT: " + responseBody.asString());
    }
}
