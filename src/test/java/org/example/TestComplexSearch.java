package org.example;

import com.sun.org.apache.xpath.internal.operations.Number;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

public class TestComplexSearch {
    private static final String API_KEY = "0970f5c615f14a2a91942df5a213e41c";

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://api.spoonacular.com/";
    }

    @Test
    void getRecipeWithQueryParameters() throws IOException {
        given()
                .log()
                .all()
                .param("apiKey", API_KEY)
                .param("query", "pasta")
                .param("maxFat", "25")
                .param("number", 2)
                .expect()
                .body("offset", is(0))
               // .body("number", isA(Number()))
                .body("totalResults", notNullValue())
                .when()
                .get("recipes/complexSearch")
                .then()
                .statusCode(200)
                .time(lessThan(5000L));
    }

    @Test
    void getRecipesByNutrients() throws IOException {
        given()
                .log()
                .all()
                .param("apiKey", API_KEY)
                .param("minCalories", "100")
                .param("maxCalories", "150")
                .param("number", 2)
                .expect()
                .body("size()", is(2))
                .body("[0].calories", greaterThanOrEqualTo(100))
                .body("[0].calories", lessThanOrEqualTo(150))
                .when()
                .get("recipes/findByNutrients")
                .then()
                .statusCode(200);
    }

    @Test
    void getRandomRecipes()throws IOException {
        given()
                .log()
                .all()
                .param("apiKey", API_KEY)
                .param("tags", "vegetarian,dessert")
                .param("number", 5)
                .expect()
                .body("recipes.size()", is(5))
                .body("recipes[0]", hasKey("id"))
                .when()
                .get("recipes/random?number=5&tags=vegetarian,dessert")
                .then()
                .statusCode(200);
    }

    @Test
    void getGuessNutritionByDishName()throws IOException {
        given()
                .log()
                .all()
                .param("apiKey", API_KEY)
                .param("title", "Spaghetti Aglio et Olio")
                .expect()
                .body("calories.value", lessThan(450F) )
                .when()
                .get("recipes/guessNutrition")
                .then()
                .statusCode(200);
    }

    @Test
    void getIngredientSubstitutesByID()throws IOException {
        given()
                .log()
                .all()
                .param("apiKey", API_KEY)
                .param("ingredientName", "butter")
                .expect()
                .body("ingredient", is("butter") )
                .body("substitutes.size()",greaterThan(0))
                .when()
                .get("/food/ingredients/1001/substitutes")
                .then()
                .statusCode(200);
    }
}
