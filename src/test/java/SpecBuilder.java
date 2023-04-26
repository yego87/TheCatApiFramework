import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class SpecBuilder {
    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder().setBaseUri("https://api.thecatapi.com")
                .setBasePath("/v1")
                .setContentType(ContentType.JSON)
                .addHeader("x-api-key", System.getProperty("x-api-key") != null ?
                        System.getProperty("x-api-key") : "live_MpJDbqeqMFmeTSfoZBbNwbfdvpfV38UD6DfZWcLpEhPmRviAvDhXthigqI5YjhBt")
                .build();
    }
}