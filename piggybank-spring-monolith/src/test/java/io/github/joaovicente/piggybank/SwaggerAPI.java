package io.github.joaovicente.piggybank;

import io.github.joaovicente.piggybank.config.SwaggerConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SwaggerAPI {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = this.port;
    }

    @Test
    public void swaggerJsonExists() throws Exception {
        Response responseBody =
                given()
                        .when()
                        .get("/v2/api-docs")
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .body("swagger", equalTo("2.0"))
                        .extract()
                        .response();
        String response = responseBody.asString();
        try (Writer writer = new FileWriter(new File("target/generated-sources/swagger.json"))) {
            writer.write(response);
        }
    }
}
