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
import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Log

public class AcceptanceTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
//        api = new RestApiUtil(port, restTemplate);
//        api.reset();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = this.port;
    }

    private String createKid(String name) {
        final int RANDOMLY_GENERATED_UUID = 4;
        String requestBody =
                "{" +
                        "\"name\":\"" + name + "\"" +
                        "}";
        Response responseBody =
                given()
                        .body(requestBody)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("/kids")
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .body("id", isA(String.class))
                        .body("name", equalTo(name))
                        .extract()
                        .response();
        String response = responseBody.asString();
        String id = from(response).getString("id");
        assertThat(UUID.fromString(id).version()).isEqualTo(RANDOMLY_GENERATED_UUID);
        log.info("POST /kids " + "IN: " + requestBody + " OUT: " + responseBody.asString());
        return id;
    }


    @SuppressWarnings("UnusedReturnValue")
    private String createTransaction(Map <String, String> transaction) {
        final int RANDOMLY_GENERATED_UUID = 4;
        String kidId = transaction.get("kidId");
        String date = transaction.get("date");
        String description = transaction.get("description");
        String kind = transaction.get("kind");
        int amount = Integer.parseInt(transaction.get("amount"));
        String requestBody =
                "{" +
                        "\"kidId\":\"" + kidId + "\"," +
                        "\"date\":\"" + date + "\"," +
                        "\"kind\":\"" + kind + "\"," +
                        "\"description\":\"" + description + "\"," +
                        "\"amount\":" + amount + "" +
                        "}";
        Response responseBody =
                given()
                        .body(requestBody)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("/transactions")
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .body("id", isA(String.class))
                        .body("kidId", equalTo(kidId))
                        .body("date", equalTo(date))
                        .body("description", equalTo(description))
                        .body("amount", equalTo(amount))
                        .extract()
                        .response();
        String response = responseBody.asString();
        String id = from(response).getString("id");
        assertThat(UUID.fromString(id).version()).isEqualTo(RANDOMLY_GENERATED_UUID);
        log.info("POST /transactions " + "IN: " + requestBody + " OUT: " + responseBody.asString());
        return id;
    }


    private void checkTransactions(List<Map<String,String>> transactionList) {
        String url = "/transactions?kidId="+ (transactionList.get(0).get("kidId"));

        Response responseBody =
                given()
                        .when()
                        .get(url)
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .body("[0].kidId", is(transactionList.get(0).get("kidId")))
                        .body("[0].date", is(transactionList.get(0).get("date")))
                        .body("[0].kind", is(transactionList.get(0).get("kind")))
                        .body("[0].description", is(transactionList.get(0).get("description")))
                        .body("[0].amount", is(Integer.parseInt(transactionList.get(0).get("amount"))))
                        .body("[1].kidId", is(transactionList.get(1).get("kidId")))
                        .body("[1].date", is(transactionList.get(1).get("date")))
                        .body("[1].kind", is(transactionList.get(1).get("kind")))
                        .body("[1].description", is(transactionList.get(1).get("description")))
                        .body("[1].amount", is(Integer.parseInt(transactionList.get(1).get("amount"))))
                        .extract()
                        .response();
        log.info("GET "+ url + " OUT: " + responseBody.asString());
    }

    private Map<String, String> buildTransaction(String kidId, String date, String kind, String description, String amount)   {
        Map<String, String> transaction = new HashMap<>();
        transaction.put("kidId", kidId);
        transaction.put("date", date);
        transaction.put("kind", kind);
        transaction.put("description",description);
        transaction.put("amount",amount);
        return transaction;
    }

    private void checkBalance(String kidId, int balance) {
        String url = "/balance?kidId="+ kidId;

        Response responseBody =
                given()
                        .when()
                        .get(url)
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .body("balance", is(balance))
                        .extract()
                        .response();
        log.info("GET "+ url + " OUT: " + responseBody.asString());
    }

    private void checkKidsAndBalances(String kid1Id, String kid1Name, int kid1Balance, String kid2Id, String kid2Name, int kid2Balance) {
        String url = "/kids-and-balances";

        Response responseBody =
                given()
                        .when()
                        .get(url)
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .body("[0].kidId", is(kid1Id))
                        .body("[0].kidName", is(kid1Name))
                        .body("[0].kidBalance", is(kid1Balance))
                        .body("[1].kidId", is(kid2Id))
                        .body("[1].kidName", is(kid2Name))
                        .body("[1].kidBalance", is(kid2Balance))
                        .extract()
                        .response();
        log.info("GET "+ url + " OUT: " + responseBody.asString());
    }

    @Test
    public void userJourney() {
        final String KID1_NAME = "Albert";
        final String KID2_NAME = "Beth";
        List<Map<String, String>>  kid1TransactionList = new ArrayList<>();
        List<Map<String, String>>  kid2TransactionList = new ArrayList<>();

        // Create kid1 - POST /kids
        final String kid1_id = createKid(KID1_NAME);
        // Create kid2 - POST /kids
        final String kid2_id = createKid(KID2_NAME);
        // create kid1 transactions - POST /transaction
        kid1TransactionList.add(buildTransaction(kid1_id, "2018-01-01", "CREDIT", "k1 credit", "100"));
        kid1TransactionList.add(buildTransaction(kid1_id, "2018-02-02", "DEBIT", "k1 debit", "50"));
        createTransaction(kid1TransactionList.get(0));
        createTransaction(kid1TransactionList.get(1));
        // Check kid1 transactions - GET /transactions?kidId={kidId}
        checkTransactions(kid1TransactionList);
        // Check kid1 balance - GET /balance?kidId={kidId}
        checkBalance(kid1_id, 50);

        // create kid2 transactions - POST /transaction
        kid2TransactionList.add(buildTransaction(kid2_id, "2018-03-03", "CREDIT", "k2 credit", "300"));
        kid2TransactionList.add(buildTransaction(kid2_id, "2018-04-04", "DEBIT", "k2 debit", "100"));
        createTransaction(kid2TransactionList.get(0));
        createTransaction(kid2TransactionList.get(1));
        // Check kid2 transactions - GET /transactions?kidId={kidId}
        checkTransactions(kid2TransactionList);
        // Check kid2 balance - GET /balance?kidId={kidId}
        checkBalance(kid2_id, 200);
        // Check kids and balances - GET /kids-and-balances
        checkKidsAndBalances(kid1_id, KID1_NAME, 50, kid2_id,KID2_NAME, 200);
    }
}
