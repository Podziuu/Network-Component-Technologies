package pl.lodz.p.edu.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.edu.rest.dto.RentDTO;
import pl.lodz.p.edu.rest.dto.CreateUserDTO;
import pl.lodz.p.edu.rest.model.Rent;
import pl.lodz.p.edu.rest.model.item.Item;
import pl.lodz.p.edu.rest.model.user.Role;
import pl.lodz.p.edu.rest.model.user.User;
import pl.lodz.p.edu.rest.repository.ItemRepository;
import pl.lodz.p.edu.rest.repository.MongoEntity;
import pl.lodz.p.edu.rest.repository.RentRepository;
import pl.lodz.p.edu.rest.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class RentIntegrationTest {
    private static final MongoEntity mongoEntity = new MongoEntity();
    private static final ItemRepository itemRepository = new ItemRepository();
    private static final UserRepository userRepository = new UserRepository();
    private static final RentRepository rentRepository = new RentRepository();

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
      RestAssured.basePath = "/api";
    }

    @AfterEach
    public void dropCollection() {
        MongoCollection<Rent> rentCollection = mongoEntity.getDatabase().getCollection("rents", Rent.class);
        MongoCollection<Item> itemCollection = mongoEntity.getDatabase().getCollection("items", Item.class);
        MongoCollection<User> userCollection = mongoEntity.getDatabase().getCollection("users", User.class);
        itemCollection.drop();
        userCollection.drop();
        rentCollection.drop();
    }

    @Test
    public void testRentItem() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.CLIENT);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .statusCode(201);

        User userFromDB = userRepository.findByLogin("testowyAdmin");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users/" + userFromDB.getId())
                .then()
                .statusCode(200)
                .body("login", equalTo("testowyAdmin"))
                .body("password", equalTo("testoweHaslo"))
                .body("firstName", equalTo("Adminek"))
                .body("lastName", equalTo("Adminowski"))
                .body("role", equalTo("CLIENT"));

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
                .post("/items")
                .then()
                .statusCode(201)
                .body("basePrice", equalTo(150))
                .body("itemName", equalTo("Kizo"))
                .body("itemType", equalTo("music"))
                .body("genre", equalTo("Classical"))
                .body("vinyl", equalTo(true));

        List<Item> items = itemRepository.getItemsByItemName("Kizo");

        if (items.isEmpty()) {
            throw new AssertionError("No items found with name Kizo");
        }

        Item item = items.get(0);

        RentDTO rentDTO = new RentDTO(100, String.valueOf(userFromDB.getId()), String.valueOf(item.getId()));

        String rentJson = new ObjectMapper().writeValueAsString(rentDTO);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/rents")
                .then()
                .statusCode(201);
    }

    @Test
    public void testReturnItem() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.CLIENT);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .statusCode(201);

        User userFromDB = userRepository.findByLogin("testowyAdmin");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users/" + userFromDB.getId())
                .then()
                .statusCode(200)
                .body("login", equalTo("testowyAdmin"))
                .body("password", equalTo("testoweHaslo"))
                .body("firstName", equalTo("Adminek"))
                .body("lastName", equalTo("Adminowski"))
                .body("role", equalTo("CLIENT"));

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
                .post("/items")
                .then()
                .statusCode(201)
                .body("basePrice", equalTo(150))
                .body("itemName", equalTo("Kizo"))
                .body("itemType", equalTo("music"))
                .body("genre", equalTo("Classical"))
                .body("vinyl", equalTo(true));

        List<Item> items = itemRepository.getItemsByItemName("Kizo");

        if (items.isEmpty()) {
            throw new AssertionError("No items found with name Kizo");
        }

        Item item = items.get(0);

        RentDTO rentDTO = new RentDTO(100, String.valueOf(userFromDB.getId()), String.valueOf(item.getId()));

        String rentJson = new ObjectMapper().writeValueAsString(rentDTO);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/rents")
                .then()
                .statusCode(201);

        List<Rent> rents = rentRepository.findActiveRents();

        Rent rent = rents.get(0);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .put("/rents/return/" + rent.getId())
                .then()
                .statusCode(204);
    }

    @Test
    public void testGetRent() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.CLIENT);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .statusCode(201);

        User userFromDB = userRepository.findByLogin("testowyAdmin");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users/" + userFromDB.getId())
                .then()
                .statusCode(200)
                .body("login", equalTo("testowyAdmin"))
                .body("password", equalTo("testoweHaslo"))
                .body("firstName", equalTo("Adminek"))
                .body("lastName", equalTo("Adminowski"))
                .body("role", equalTo("CLIENT"));

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
                .post("/items")
                .then()
                .statusCode(201)
                .body("basePrice", equalTo(150))
                .body("itemName", equalTo("Kizo"))
                .body("itemType", equalTo("music"))
                .body("genre", equalTo("Classical"))
                .body("vinyl", equalTo(true));

        List<Item> items = itemRepository.getItemsByItemName("Kizo");

        if (items.isEmpty()) {
            throw new AssertionError("No items found with name Kizo");
        }

        Item item = items.get(0);

        RentDTO rentDTO = new RentDTO(100, String.valueOf(userFromDB.getId()), String.valueOf(item.getId()));

        String rentJson = new ObjectMapper().writeValueAsString(rentDTO);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/rents")
                .then()
                .statusCode(201);

        List<Rent> rents = rentRepository.findActiveRents();

        Rent rent = rents.get(0);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/rents/" + rent.getId())
                .then()
                .statusCode(200)
                .body("rentCost", equalTo(100))
                .body("clientId", equalTo(String.valueOf(userFromDB.getId())))
                .body("itemId", equalTo(String.valueOf(item.getId())));
    }

    @Test
    public void testGetActiveRents() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.CLIENT);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .statusCode(201);

        User userFromDB = userRepository.findByLogin("testowyAdmin");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users/" + userFromDB.getId())
                .then()
                .statusCode(200)
                .body("login", equalTo("testowyAdmin"))
                .body("password", equalTo("testoweHaslo"))
                .body("firstName", equalTo("Adminek"))
                .body("lastName", equalTo("Adminowski"))
                .body("role", equalTo("CLIENT"));

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
                .post("/items")
                .then()
                .statusCode(201)
                .body("basePrice", equalTo(150))
                .body("itemName", equalTo("Kizo"))
                .body("itemType", equalTo("music"))
                .body("genre", equalTo("Classical"))
                .body("vinyl", equalTo(true));

        List<Item> items = itemRepository.getItemsByItemName("Kizo");

        if (items.isEmpty()) {
            throw new AssertionError("No items found with name Kizo");
        }

        Item item = items.get(0);

        RentDTO rentDTO = new RentDTO(100, String.valueOf(userFromDB.getId()), String.valueOf(item.getId()));

        String rentJson = new ObjectMapper().writeValueAsString(rentDTO);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/rents")
                .then()
                .statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/rents/active")
                .then()
                .statusCode(200)
                .body("[0].rentCost", equalTo(100))
                .body("[0].clientId", equalTo(String.valueOf(userFromDB.getId())))
                .body("[0].itemId", equalTo(String.valueOf(item.getId())));
    }

    @Test
    public void testGetInactiveRents() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.CLIENT);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .statusCode(201);

        User userFromDB = userRepository.findByLogin("testowyAdmin");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users/" + userFromDB.getId())
                .then()
                .statusCode(200)
                .body("login", equalTo("testowyAdmin"))
                .body("password", equalTo("testoweHaslo"))
                .body("firstName", equalTo("Adminek"))
                .body("lastName", equalTo("Adminowski"))
                .body("role", equalTo("CLIENT"));

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
                .post("/items")
                .then()
                .statusCode(201)
                .body("basePrice", equalTo(150))
                .body("itemName", equalTo("Kizo"))
                .body("itemType", equalTo("music"))
                .body("genre", equalTo("Classical"))
                .body("vinyl", equalTo(true));

        List<Item> items = itemRepository.getItemsByItemName("Kizo");

        if (items.isEmpty()) {
            throw new AssertionError("No items found with name Kizo");
        }

        Item item = items.get(0);

        RentDTO rentDTO = new RentDTO(100, String.valueOf(userFromDB.getId()), String.valueOf(item.getId()));

        String rentJson = new ObjectMapper().writeValueAsString(rentDTO);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/rents")
                .then()
                .statusCode(201);

        List<Rent> rents = rentRepository.findActiveRents();

        Rent rent = rents.get(0);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .put("/rents/return/" + rent.getId())
                .then()
                .statusCode(204);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/rents/inactive")
                .then()
                .statusCode(200)
                .body("[0].rentCost", equalTo(100))
                .body("[0].clientId", equalTo(String.valueOf(userFromDB.getId())))
                .body("[0].itemId", equalTo(String.valueOf(item.getId())));
    }

    @Test
    public void testRentItemAlreadyRented() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.CLIENT);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .statusCode(201);

        User userFromDB = userRepository.findByLogin("testowyAdmin");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users/" + userFromDB.getId())
                .then()
                .statusCode(200)
                .body("login", equalTo("testowyAdmin"))
                .body("password", equalTo("testoweHaslo"))
                .body("firstName", equalTo("Adminek"))
                .body("lastName", equalTo("Adminowski"))
                .body("role", equalTo("CLIENT"));

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
                .post("/items")
                .then()
                .statusCode(201)
                .body("basePrice", equalTo(150))
                .body("itemName", equalTo("Kizo"))
                .body("itemType", equalTo("music"))
                .body("genre", equalTo("Classical"))
                .body("vinyl", equalTo(true));

        List<Item> items = itemRepository.getItemsByItemName("Kizo");

        if (items.isEmpty()) {
            throw new AssertionError("No items found with name Kizo");
        }

        Item item = items.get(0);

        RentDTO rentDTO = new RentDTO(100, String.valueOf(userFromDB.getId()), String.valueOf(item.getId()));

        String rentJson = new ObjectMapper().writeValueAsString(rentDTO);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(rentJson)
                .when()
                .post("/rents")
                .then()
                .statusCode(201);

        RentDTO rentDTO1 = new RentDTO(100, String.valueOf(userFromDB.getId()), String.valueOf(item.getId()));

        String rentJson1 = new ObjectMapper().writeValueAsString(rentDTO1);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(rentJson1)
                .when()
                .post("/rents")
                .then()
                .statusCode(409);
    }

}
