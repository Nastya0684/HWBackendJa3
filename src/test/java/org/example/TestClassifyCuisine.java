package org.example;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestClassifyCuisine {
    private static final String API_KEY = "0970f5c615f14a2a91942df5a213e41c";

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://api.spoonacular.com/recipes/";
    }

    @Test
    void postResponseLanguage() throws IOException {
        given()
                .header("x-api-key", API_KEY)
                .urlEncodingEnabled(true)
                .param("title", "рис карри")
                .param("ingredientList", "карри рис")
                .param("language", "ru")
                .expect()
                .body("confidence", is(0F))
                .when()
                .post("cuisine")
                .then()
                .statusCode(200);
    }

   @Test
    void postResponseTime() throws IOException {
       given()
               .header("x-api-key", API_KEY)
               .urlEncodingEnabled(true)
               .param("title", "curry rice")
               .param("ingredientList", "rice curry")
               .param("language", "en")
                .expect()
                .when()
                .post("cuisine")
                .then()
                .statusCode(200)
                .time(lessThan(5000L));
    }

    @Test
    void postContentTypeHeader() throws IOException {
        given()
                .header("x-api-key", API_KEY)
                .urlEncodingEnabled(true)
                .param("title", "curry rice")
                .param("ingredientList", "rice curry")
                .param("language", "en")
                .expect()
                .header("Content-type", "application/json")
                .when()
                .post("cuisine")
                .then()
                .statusCode(200);
    }

    @Test
    void postDataTypeResponse() throws IOException {
        given()
                .header("x-api-key", API_KEY)
                .urlEncodingEnabled(true)
                .param("title", "curry rice")
                .param("ingredientList", "rice curry")
                .param("language", "en")
                .expect()
                .body("cuisines[0]",is("Indian"))
                .body("confidence", greaterThanOrEqualTo(0.9F))
                .when()
                .post("cuisine")
                .then()
                .statusCode(200);
    }
}
