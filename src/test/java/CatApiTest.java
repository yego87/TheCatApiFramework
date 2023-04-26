import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Execution(ExecutionMode.CONCURRENT)
public class CatApiTest {

    @Test
    public void testCase_1() {
        // 1. Выполняем GET запрос к /images/search
        List<Map<String, Object>> breeds = given(SpecBuilder.getRequestSpec()).param("limit=20")
                .when().get("/breeds/").as(new TypeRef<>() {});
        String breedId = "";
        for (Map<String, Object> breed : breeds) {
            if (breed.get("name") == "") breedId = breed.get("id").toString();
        }
        // 2. Выполняем GET запрос к /images/search
        Response response = given(SpecBuilder.getRequestSpec()).param("breed_ids=" + breedId)
                .when().get("/images/search");

        String imageId = response.path("[0].id");
        String imageUrl = response.path("[0].url");

        // 3. Выполняем POST запрос к /favourites
        response = given(SpecBuilder.getRequestSpec()).body("{ \"image_id\": \"" + imageId + "\"}")
                .when().post("/favourites")
                .then().body("message", equalTo("SUCCESS")).extract().response();
        Integer favouriteId = response.body().path("id");

        // 4. Выполняем GET запрос к /favourites
        given(SpecBuilder.getRequestSpec())
                .when().get("/favourites/" + favouriteId)
                .then().body("id", equalTo(favouriteId)).body("image.url", equalTo(imageUrl));

        // 5. Выполняем DELETE запрос
        given(SpecBuilder.getRequestSpec())
                .when().delete("/favourites/" + favouriteId)
                .then().body("message", equalTo("SUCCESS"));

        // 6. Выполняем GET запрос
        given(SpecBuilder.getRequestSpec())
                .when().get("/favourites/" + favouriteId)
                .then().statusCode(404);
    }

    @Test
    public void testCase_2() {
        // 1. Выполняем GET запрос к /categories
        given(SpecBuilder.getRequestSpec())
                .when().get("/categories")
                .then().body("name", hasItem("boxes"));
    }
}
