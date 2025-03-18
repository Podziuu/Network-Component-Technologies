package pl.lodz.p.edu.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.lodz.p.edu.rest.dto.UpdateUserDTO;
import pl.lodz.p.edu.rest.dto.CreateUserDTO;
import pl.lodz.p.edu.rest.model.user.Role;
import pl.lodz.p.edu.rest.model.user.User;
import pl.lodz.p.edu.rest.repository.MongoEntity;
import pl.lodz.p.edu.rest.repository.UserRepository;

import static org.hamcrest.Matchers.equalTo;

public class UserIntegrationTest {
    private static final MongoEntity mongoEntity = new MongoEntity();
    private static final UserRepository userRepository = new UserRepository();

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/api/users";
    }

    @AfterEach
    public void dropCollection() {
        MongoCollection<User> userCollection = mongoEntity.getDatabase().getCollection("users", User.class);
        userCollection.drop();
    }

    @Test
    public void testCreateUser() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.ADMIN);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("")
                .then()
                .statusCode(201)
                .body("login", equalTo("testowyAdmin"))
                .body("password", equalTo("testoweHaslo"))
                .body("firstName", equalTo("Adminek"))
                .body("lastName", equalTo("Adminowski"))
                .body("role", equalTo("ADMIN"));
    }

    @Test
    public void testCreateUserWithMissingProperty() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                null,
                Role.ADMIN);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("")
                .then()
                .statusCode(500);
    }

    @Test
    public void testGetUser() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.ADMIN);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("")
                .then()
                .statusCode(201);

        User userFromDB = userRepository.findByLogin("testowyAdmin");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/" + userFromDB.getId())
                .then()
                .statusCode(200)
                .body("login", equalTo("testowyAdmin"))
                .body("password", equalTo("testoweHaslo"))
                .body("firstName", equalTo("Adminek"))
                .body("lastName", equalTo("Adminowski"))
                .body("role", equalTo("ADMIN"));
    }

    @Test
    public void testGetUsersByFirstName() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.ADMIN);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("")
                .then()
                .statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .param("firstName", "Adm")
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("[0].login", equalTo("testowyAdmin"))
                .body("[0].password", equalTo("testoweHaslo"))
                .body("[0].firstName", equalTo("Adminek"))
                .body("[0].lastName", equalTo("Adminowski"))
                .body("[0].role", equalTo("ADMIN"));
    }

    @Test
    public void testGetUsersByFirstNameError() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.ADMIN);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("")
                .then()
                .statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .param("firstName", "NieAdm")
                .when()
                .get()
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetUsersByFirstNameAndRoleError() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.ADMIN);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("")
                .then()
                .statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .param("firstName", "NieAdm")
                .param("role", Role.ADMIN)
                .when()
                .get()
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetUsersByFirstNameAndRole() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.ADMIN);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("")
                .then()
                .statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .param("firstName", "Adm")
                .param("role", Role.ADMIN)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("[0].login", equalTo("testowyAdmin"))
                .body("[0].password", equalTo("testoweHaslo"))
                .body("[0].firstName", equalTo("Adminek"))
                .body("[0].lastName", equalTo("Adminowski"))
                .body("[0].role", equalTo("ADMIN"));
    }

    @Test
    public void testGetAllUsers() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.ADMIN);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("")
                .then()
                .statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("[0].login", equalTo("testowyAdmin"))
                .body("[0].password", equalTo("testoweHaslo"))
                .body("[0].firstName", equalTo("Adminek"))
                .body("[0].lastName", equalTo("Adminowski"))
                .body("[0].role", equalTo("ADMIN"));
    }

    @Test
    public void testGetAllUsersByRole() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.ADMIN);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("")
                .then()
                .statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .param("role", Role.ADMIN)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("[0].login", equalTo("testowyAdmin"))
                .body("[0].password", equalTo("testoweHaslo"))
                .body("[0].firstName", equalTo("Adminek"))
                .body("[0].lastName", equalTo("Adminowski"))
                .body("[0].role", equalTo("ADMIN"));
    }

    @Test
    public void testUpdateUser() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.ADMIN);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("")
                .then()
                .statusCode(201);

        User userFromDB = userRepository.findByLogin("testowyAdmin");

        UpdateUserDTO updatedUser = new UpdateUserDTO("innyAdminek", "innyAdminowski");

        String updatedUserJson = new ObjectMapper().writeValueAsString(updatedUser);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatedUserJson)
                .when()
                .put("/" + userFromDB.getId())
                .then()
                .statusCode(204);
    }

    @Test
    public void testActivateUser() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.ADMIN);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("")
                .then()
                .statusCode(201);

        User userFromDB = userRepository.findByLogin("testowyAdmin");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .put("/deactivate/" + userFromDB.getId())
                .then()
                .statusCode(204);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .put("/activate/" + userFromDB.getId())
                .then()
                .statusCode(204);
    }

    @Test
    public void testDeactivateUser() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.ADMIN);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("")
                .then()
                .statusCode(201);

        User userFromDB = userRepository.findByLogin("testowyAdmin");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .put("/deactivate/" + userFromDB.getId())
                .then()
                .statusCode(204);
    }

    @Test
    public void testCreateUserWithExistingLogin() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin",
                "testoweHaslo",
                "Adminek",
                "Adminowski",
                Role.ADMIN);

        String userJson = new ObjectMapper().writeValueAsString(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("")
                .then()
                .statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("")
                .then()
                .statusCode(409);
    }
}
