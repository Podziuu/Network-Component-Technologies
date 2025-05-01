import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.tks.ports.infrastructure.ItemPort;
import pl.tks.ports.infrastructure.UserPort;
import pl.tks.ports.infrastructure.RentPort;
import pl.tks.rest.RentRestAdapterApplication;

import java.util.HashMap;
import java.util.Map;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RentRestAdapterApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RentIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    private ItemPort itemPort;

    @Autowired
    private UserPort clientPort;

    @Autowired
    private RentPort rentPort;

    private String authToken;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    private String username;
    private String password = "admin123";

    @BeforeEach
    void setup() throws Exception {
        rentPort.deleteAll();
        itemPort.deleteAllItems();
        clientPort.deleteAll();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api";

        username = "adminek";

        Map<String, Object> registerPayload = new HashMap<>();
        registerPayload.put("login", username);
        registerPayload.put("password", password);
        registerPayload.put("firstName", "Jurek");
        registerPayload.put("lastName", "Kiler");
        registerPayload.put("role", "CLIENT");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(toJson(registerPayload))
                .post("/users")
                .then().statusCode(201);

        Map<String, Object> loginPayload = new HashMap<>();
        loginPayload.put("login", username);
        loginPayload.put("password", password);

        authToken = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(toJson(loginPayload))
                .post("/users/login")
                .then().statusCode(200)
                .extract().path("token");
    }
}
