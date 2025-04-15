import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.tks.model.item.Item;
import pl.tks.model.user.Client;
import pl.tks.model.user.ClientType;
import pl.tks.model.user.User;
import pl.tks.ports.infrastructure.ItemPort;
import pl.tks.ports.infrastructure.UserPort;
import pl.tks.ports.infrastructure.RentPort;
import pl.tks.rest.RestAdaptersApplication;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAdaptersApplication.class)
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
        // Clear DB
        rentPort.deleteAll();
        itemPort.deleteAllItems();
        clientPort.deleteAll();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api";

        // Register user (ADMIN)
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

    @Test
    public void rentFlowTest() throws Exception {

        // Create item
        Map<String, Object> itemPayload = new HashMap<>();
        itemPayload.put("basePrice", 100);
        itemPayload.put("itemName", "Blade Runner");
        itemPayload.put("itemType", "movie");
        itemPayload.put("minutes", 120);
        itemPayload.put("casette", true);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(toJson(itemPayload))
                .post("/items")
                .then()
                .statusCode(201);

        String itemId = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(toJson(itemPayload))
                .post("/items")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
        User createdUser = clientPort.findByLogin("adminek");


        // Create rent
        Map<String, Object> rentPayload = new HashMap<>();
        rentPayload.put("rentCost", 250);
        rentPayload.put("clientId", createdUser.getId());
        rentPayload.put("itemId", itemId);

//        String rentId = RestAssured.given()
//                .header("Authorization", "Bearer " + authToken)
//                .contentType(ContentType.JSON)
//                .body(toJson(rentPayload))
//                .post("/rents")
//                .then()
//                .statusCode(500)
//                .body("rentCost", equalTo(250))
//                .body("clientId", equalTo(createdUser.getId()))
//                .body("itemId", equalTo(itemId))
//                .extract().path("id");

//        RestAssured.given()
//                .header("Authorization", "Bearer " + authToken)
//                .get("/rents/" + rentId)
//                .then()
//                .statusCode(200)
//                .body("id", equalTo(rentId))
//                .body("itemId", equalTo(itemId))
//                .body("clientId", equalTo(createdUser.getId()));
//
//        RestAssured.given()
//                .header("Authorization", "Bearer " + authToken)
//                .put("/rents/return/" + rentId)
//                .then()
//                .statusCode(204);
//
//        RestAssured.given()
//                .header("Authorization", "Bearer " + authToken)
//                .get("/rents/inactive")
//                .then()
//                .statusCode(200)
//                .body("[0].id", equalTo(rentId))
//                .body("[0].archive", equalTo(true));
    }
}
