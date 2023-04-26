import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class SpecBuilder {
    public static RequestSpecification getRequestSpec() throws Exception {
        if(System.getProperty("x-api-key") == null)  throw new Exception("Please enter x-api-key");
        return new RequestSpecBuilder().setBaseUri("https://api.thecatapi.com")
                .setBasePath("/v1")
                .setContentType(ContentType.JSON)
                .addHeader("x-api-key", System.getProperty("x-api-key"))
                .build();
    }
}