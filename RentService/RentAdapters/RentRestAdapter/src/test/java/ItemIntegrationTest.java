//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import pl.tks.modelrent.item.Item;
//import pl.tks.portsrent.infrastructure.ItemPort;
//import pl.tks.restrent.RentRestAdapterApplication;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.hamcrest.Matchers.equalTo;
//@ActiveProfiles("test")
//@Testcontainers
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RentRestAdapterApplication.class)
//public class ItemIntegrationTest extends AbstractRestTest {
//
//    static {
//        System.setProperty("mongo.uri", mongoDBContainer.getReplicaSetUrl());
//        System.setProperty("mongo.database", "user_integration_test");
//        System.clearProperty("mongo.username");
//        System.clearProperty("mongo.password");
//        System.clearProperty("mongo.authDb");
//    }
//
//    @LocalServerPort
//    int port;
//
//    @Autowired
//    ItemPort itemPort;
//
//    @BeforeEach
//    public void setup() {
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = port;
//        RestAssured.basePath = "/api";
//        itemPort.deleteAllItems(); // Czyszczenie przed każdym testem
//    }
//
//    @AfterEach
//    public void cleanUp() {
//        itemPort.deleteAllItems(); // Czyszczenie po każdym teście
//    }
//
//    private String toJson(Map<String, Object> map) throws JsonProcessingException {
//        return new ObjectMapper().writeValueAsString(map);
//    }
//
//    @Test
//    public void testCreateMusic() throws JsonProcessingException {
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("basePrice", 150);
//        payload.put("itemName", "Kizo");
//        payload.put("itemType", "music");
//        payload.put("genre", 2);
//        payload.put("vinyl", true);
//
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(toJson(payload))
//                .when()
//                .post("/items")
//                .then()
//                .statusCode(201)
//                .body("basePrice", equalTo(150))
//                .body("itemName", equalTo("Kizo"))
//                .body("itemType", equalTo("music"))
//                .body("genre", equalTo("Classical"))
//                .body("vinyl", equalTo(true));
//    }
//
//    @Test
//    public void testCreateMovie() throws JsonProcessingException {
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("basePrice", 111);
//        payload.put("itemName", "Skazani");
//        payload.put("itemType", "movie");
//        payload.put("minutes", 231);
//        payload.put("casette", true);
//
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(toJson(payload))
//                .when()
//                .post("/items")
//                .then()
//                .statusCode(201)
//                .body("basePrice", equalTo(111))
//                .body("itemName", equalTo("Skazani"))
//                .body("itemType", equalTo("movie"))
//                .body("minutes", equalTo(231))
//                .body("casette", equalTo(true));
//    }
//
//    @Test
//    public void testCreateComics() throws JsonProcessingException {
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("basePrice", 150);
//        payload.put("itemName", "Scooby");
//        payload.put("itemType", "comics");
//        payload.put("pagesNumber", 222);
//
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(toJson(payload))
//                .when()
//                .post("/items")
//                .then()
//                .statusCode(201)
//                .body("basePrice", equalTo(150))
//                .body("itemName", equalTo("Scooby"))
//                .body("itemType", equalTo("comics"))
//                .body("pagesNumber", equalTo(222));
//    }
//
//    @Test
//    public void testGetMusic() throws JsonProcessingException {
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("basePrice", 150);
//        payload.put("itemName", "Scooby");
//        payload.put("itemType", "music");
//        payload.put("genre", 2);
//        payload.put("vinyl", true);
//
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(toJson(payload))
//                .when()
//                .post("/items")
//                .then()
//                .statusCode(201);
//
//        List<Item> items = itemPort.getItemsByItemName("Scooby");
//
//        Item item = items.stream().findFirst().orElseThrow();
//
//        RestAssured.given()
//                .when()
//                .get("/items/" + item.getId())
//                .then()
//                .statusCode(200)
//                .body("basePrice", equalTo(150))
//                .body("itemName", equalTo("Scooby"))
//                .body("itemType", equalTo("music"))
//                .body("genre", equalTo("Classical"))
//                .body("vinyl", equalTo(true));
//    }
//
//    @Test
//    public void testGetAllItems() throws JsonProcessingException {
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("basePrice", 150);
//        payload.put("itemName", "Scooby");
//        payload.put("itemType", "music");
//        payload.put("genre", 2);
//        payload.put("vinyl", true);
//
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(toJson(payload))
//                .when()
//                .post("/items")
//                .then()
//                .statusCode(201);
//
//        RestAssured.given()
//                .when()
//                .get("/items")
//                .then()
//                .statusCode(200)
//                .body("[0].basePrice", equalTo(150))
//                .body("[0].itemName", equalTo("Scooby"))
//                .body("[0].itemType", equalTo("music"))
//                .body("[0].genre", equalTo("Classical"))
//                .body("[0].vinyl", equalTo(true));
//    }
//
//    @Test
//    public void testUpdateMusic() throws JsonProcessingException {
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("basePrice", 150);
//        payload.put("itemName", "Scooby");
//        payload.put("itemType", "music");
//        payload.put("genre", 2);
//        payload.put("vinyl", true);
//
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(toJson(payload))
//                .when()
//                .post("/items")
//                .then()
//                .statusCode(201);
//
//        Item item = itemPort.getItemsByItemName("Scooby").stream().findFirst().orElseThrow();
//
//        Map<String, Object> updated = new HashMap<>();
//        updated.put("basePrice", 169);
//        updated.put("itemName", "Scooby");
//        updated.put("itemType", "music");
//        updated.put("genre", 2);
//        updated.put("vinyl", true);
//
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(toJson(updated))
//                .when()
//                .put("/items/" + item.getId())
//                .then()
//                .statusCode(204);
//    }
//
//    @Test
//    public void testDeleteItem() throws JsonProcessingException {
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("basePrice", 150);
//        payload.put("itemName", "Scooby");
//        payload.put("itemType", "music");
//        payload.put("genre", 2);
//        payload.put("vinyl", true);
//
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(toJson(payload))
//                .when()
//                .post("/items")
//                .then()
//                .statusCode(201);
//
//        Item item = itemPort.getItemsByItemName("Scooby").stream().findFirst().orElseThrow();
//
//        RestAssured.given()
//                .when()
//                .delete("/items/" + item.getId())
//                .then()
//                .statusCode(204);
//    }
//}
