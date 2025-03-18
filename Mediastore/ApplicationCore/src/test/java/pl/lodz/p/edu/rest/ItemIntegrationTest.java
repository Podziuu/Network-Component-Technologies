package pl.lodz.p.edu.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.edu.rest.model.item.Item;
import pl.lodz.p.edu.rest.repository.ItemRepository;
import pl.lodz.p.edu.rest.repository.MongoEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class ItemIntegrationTest {
    private static final MongoEntity mongoEntity = new MongoEntity();
    private static final ItemRepository itemRepository = new ItemRepository();

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/api/items";
    }

    @AfterEach
    public void dropCollection() {
        MongoCollection<Item> itemCollection = mongoEntity.getDatabase().getCollection("items", Item.class);
        itemCollection.drop();
    }

    @Test
    public void testCreateMusic() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("basePrice", 150);
        payload.put("itemName", "Kizo");
        payload.put("itemType", "music");
        payload.put("genre", 2);
        payload.put("vinyl", true);

        String payloadJson = new ObjectMapper().writeValueAsString(payload);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post("")
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

        String payloadJson = new ObjectMapper().writeValueAsString(payload);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post("")
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

        String payloadJson = new ObjectMapper().writeValueAsString(payload);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post("")
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

        String payloadJson = new ObjectMapper().writeValueAsString(payload);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post("")
                .then()
                .statusCode(201);


        List<Item> items = itemRepository.getItemsByItemName("Scooby");

        if (items.isEmpty()) {
            throw new AssertionError("No items found with name Scooby");
        }

        Item item = items.get(0);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/" + item.getId())
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

        String payloadJson = new ObjectMapper().writeValueAsString(payload);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post("")
                .then()
                .statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("")
                .then()
                .statusCode(200)
                .body("[0].basePrice", equalTo(150))
                .body("[0].itemName", equalTo("Scooby"))
                .body("[0].itemType", equalTo("music"))
                .body("[0].genre", equalTo("Classical"))
                .body("[0].vinyl", equalTo(true));
    }

    @Test
    public void testGetAllItemsByItemType() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("basePrice", 150);
        payload.put("itemName", "Scoobyxxx");
        payload.put("itemType", "comics");
        payload.put("pagesNumber", 222);

        String payloadJson = new ObjectMapper().writeValueAsString(payload);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post("")
                .then()
                .statusCode(201)
                .body("basePrice", equalTo(150))
                .body("itemName", equalTo("Scoobyxxx"))
                .body("itemType", equalTo("comics"))
                .body("pagesNumber", equalTo(222));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/type/comics")
                .then()
                .statusCode(200)
                .body("[0].basePrice", equalTo(150))
                .body("[0].itemName", equalTo("Scoobyxxx"))
                .body("[0].itemType", equalTo("comics"))
                .body("[0].pagesNumber", equalTo(222));
    }

    @Test
    public void testUpdateMusic() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("basePrice", 150);
        payload.put("itemName", "Scooby");
        payload.put("itemType", "music");
        payload.put("genre", 2);
        payload.put("vinyl", true);

        String payloadJson = new ObjectMapper().writeValueAsString(payload);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post("")
                .then()
                .statusCode(201);

        List<Item> items = itemRepository.getItemsByItemName("Scooby");

        if (items.isEmpty()) {
            throw new AssertionError("No items found with name Scooby");
        }

        Item item = items.get(0);

        Map<String, Object> payload1 = new HashMap<>();
        payload1.put("basePrice", 169);
        payload1.put("itemName", "Scooby");
        payload1.put("itemType", "music");
        payload1.put("genre", 2);
        payload1.put("vinyl", true);

        String payloadJson1 = new ObjectMapper().writeValueAsString(payload1);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson1)
                .when()
                .put("/" + item.getId())
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

        String payloadJson = new ObjectMapper().writeValueAsString(payload);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post("")
                .then()
                .statusCode(201);

        List<Item> items = itemRepository.getItemsByItemName("Scooby");

        if (items.isEmpty()) {
            throw new AssertionError("No items found with name Scooby");
        }

        Item item = items.get(0);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/" + item.getId())
                .then()
                .statusCode(204);

    }

    @Test
    public void testAddItemWrong() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("basePrice", 150);
        payload.put("item", "Scooby");
        payload.put("type", "music");
        payload.put("genre", 88);
        payload.put("vinyl", true);

        String payloadJson = new ObjectMapper().writeValueAsString(payload);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payloadJson)
                .when()
                .post("")
                .then()
                .statusCode(500);
    }
}