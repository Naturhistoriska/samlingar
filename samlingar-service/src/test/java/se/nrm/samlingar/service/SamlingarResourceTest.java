package se.nrm.samlingar.service;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class SamlingarResourceTest {
    @Test
    void testHealthEndpoint() {
        given()
          .when().get("/")
          .then()
             .statusCode(200)
             .body(is("{ \"status\": \"ok\", \"message\": \"Museum Search API online\" }"));
    }
}