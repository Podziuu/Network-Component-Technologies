import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.tks.model.item.Item;
import pl.tks.ports.infrastructure.ItemPort;
import pl.tks.rest.RentRestAdapterApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RentRestAdapterApplication.class)
@ActiveProfiles("test")
public class ItemIntegrationTest extends AbstractRestTest {
    private String authToken;

    @LocalServerPort
    int port;

    @Autowired
    ItemPort itemPort;

    private String username;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() throws JsonProcessingException {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api";

        username = "admin_" + System.currentTimeMillis();
        String password = "admin123";

        Map<String, Object> registerPayload = new HashMap<>();
        registerPayload.put("login", username);
        registerPayload.put("password", password);
        registerPayload.put("firstName", "Jurek");
        registerPayload.put("lastName", "Kiler");
        registerPayload.put("role", "ADMIN");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(toJson(registerPayload))
                .when()
                .post("/users")
                .then()
                .statusCode(201);

        Map<String, Object> loginPayload = new HashMap<>();
        loginPayload.put("login", username);
        loginPayload.put("password", password);

        authToken = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(toJson(loginPayload))
                .when()
                .post("/users/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
    @BeforeEach
    public void prepare() {
        itemPort.deleteAllItems();
    }
    @AfterEach
    public void cleanUp() {
        itemPort.deleteAllItems();
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    public void testCreateMusic() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("basePrice", 150);
        payload.put("itemName", "Kizo");
        payload.put("itemType", "music");
        payload.put("genre", 2);
        payload.put("vinyl", true);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(toJson(payload))
                .when()
                .post("/items")
                .then()
                .statusCode(201)
                .body("basePrice", equalTo(150))
                .body("itemName", equalTo("Kizo"))
                .body("itemType", equalTo("music"))
                .body("genre", equalTo("Classical"))
                .body("vinyl", equalTo(true));

    }

    @Test
    public void testCreateMovie() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("basePrice", 111);
        payload.put("itemName", "Skazani");
        payload.put("itemType", "movie");
        payload.put("minutes", 231);
        payload.put("casette", true);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(toJson(payload))
                .when()
                .post("/items")
                .then()
                .statusCode(201)
                .body("basePrice", equalTo(111))
                .body("itemName", equalTo("Skazani"))
                .body("itemType", equalTo("movie"))
                .body("minutes", equalTo(231))
                .body("casette", equalTo(true));
    }

    @Test
    public void testCreateComics() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("basePrice", 150);
        payload.put("itemName", "Scooby");
        payload.put("itemType", "comics");
        payload.put("pagesNumber", 222);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(toJson(payload))
                .when()
                .post("/items")
                .then()
                .statusCode(201)
                .body("basePrice", equalTo(150))
                .body("itemName", equalTo("Scooby"))
                .body("itemType", equalTo("comics"))
                .body("pagesNumber", equalTo(222));
    }

    @Test
    public void testGetMusic() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("basePrice", 150);
        payload.put("itemName", "Scooby");
        payload.put("itemType", "music");
        payload.put("genre", 2);
        payload.put("vinyl", true);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(toJson(payload))
                .when()
                .post("/items")
                .then()
                .statusCode(201);

        List<Item> items = itemPort.getItemsByItemName("Scooby");

        Item item = items.stream().findFirst().orElseThrow();

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/items/" + item.getId())
                .then()
                .statusCode(200)
                .body("basePrice", equalTo(150))
                .body("itemName", equalTo("Scooby"))
                .body("itemType", equalTo("music"))
                .body("genre", equalTo("Classical"))
                .body("vinyl", equalTo(true));
    }

    @Test
    public void testGetAllItems() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("basePrice", 150);
        payload.put("itemName", "Scooby");
        payload.put("itemType", "music");
        payload.put("genre", 2);
        payload.put("vinyl", true);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(toJson(payload))
                .when()
                .post("/items")
                .then()
                .statusCode(201);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .body("[0].basePrice", equalTo(150))
                .body("[0].itemName", equalTo("Scooby"))
                .body("[0].itemType", equalTo("music"))
                .body("[0].genre", equalTo("Classical"))
                .body("[0].vinyl", equalTo(true));
    }

    @Test
    public void testUpdateMusic() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("basePrice", 150);
        payload.put("itemName", "Scooby");
        payload.put("itemType", "music");
        payload.put("genre", 2);
        payload.put("vinyl", true);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(toJson(payload))
                .when()
                .post("/items")
                .then()
                .statusCode(201);

        Item item = itemPort.getItemsByItemName("Scooby").stream().findFirst().orElseThrow();

        Map<String, Object> updated = new HashMap<>();
        updated.put("basePrice", 169);
        updated.put("itemName", "Scooby");
        updated.put("itemType", "music");
        updated.put("genre", 2);
        updated.put("vinyl", true);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(toJson(updated))
                .when()
                .put("/items/" + item.getId())
                .then()
                .statusCode(204);
    }

    @Test
    public void testDeleteItem() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("basePrice", 150);
        payload.put("itemName", "Scooby");
        payload.put("itemType", "music");
        payload.put("genre", 2);
        payload.put("vinyl", true);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(toJson(payload))
                .when()
                .post("/items")
                .then()
                .statusCode(201);

        Item item = itemPort.getItemsByItemName("Scooby").stream().findFirst().orElseThrow();

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .delete("/items/" + item.getId())
                .then()
                .statusCode(204);
    }
}
