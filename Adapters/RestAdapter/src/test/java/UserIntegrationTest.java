import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.tks.model.user.Role;
import pl.tks.model.user.User;
import pl.tks.ports.infrastructure.UserPort;
import pl.tks.repos.repo.UserRepository;
import pl.tks.rest.RestAdaptersApplication;
import pl.tks.rest.dto.CreateUserDTO;
import pl.tks.rest.dto.UpdateUserDTO;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAdaptersApplication.class)
@ActiveProfiles("test")
public class UserIntegrationTest {

    private String authToken;
    private String username;

    @LocalServerPort
    int port;

    @Autowired
    UserPort userPort;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() throws JsonProcessingException {
        userPort.deleteAll();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api/users";

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
                .post("")
                .then()
                .statusCode(201);

        Map<String, Object> loginPayload = new HashMap<>();
        loginPayload.put("login", username);
        loginPayload.put("password", password);

        authToken = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(toJson(loginPayload))
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @AfterEach
    public void cleanUp() {
        userPort.deleteAll();
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    public void testCreateUserWithExistingLogin() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin", "testoweHaslo", "Adminek", "Adminowski", Role.ADMIN);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(toJson(user))
                .when()
                .post("")
                .then()
                .statusCode(201);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(toJson(user))
                .when()
                .post("")
                .then()
                .statusCode(409);
    }

    @Test
    public void testUpdateUser() throws JsonProcessingException {
        username = "adminek1";
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
                .post("")
                .then()
                .statusCode(201);

        User createdUser = userPort.findByLogin("adminek1");

        String etag = RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/" + createdUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .header("ETag");

        UpdateUserDTO update = new UpdateUserDTO("Nowy", "Nazwisko");

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .header("ETag", etag)
                .contentType(ContentType.JSON)
                .body(toJson(update))
                .when()
                .put("/" + createdUser.getId())
                .then()
                .statusCode(204);
    }



    @Test
    public void testDeactivateAndActivateUser() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin", "testoweHaslo", "Adminek", "Adminowski", Role.ADMIN);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(toJson(user))
                .when()
                .post("")
                .then()
                .statusCode(201);

        User createdUser = userPort.findByLogin("testowyAdmin");

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .put("/deactivate/" + createdUser.getId())
                .then()
                .statusCode(204);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .put("/activate/" + createdUser.getId())
                .then()
                .statusCode(204);
    }

    @Test
    public void testGetUserById() throws JsonProcessingException {
        CreateUserDTO user = new CreateUserDTO("testowyAdmin", "testoweHaslo", "Adminek", "Adminowski", Role.ADMIN);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(toJson(user))
                .when()
                .post("")
                .then()
                .statusCode(201);

        User createdUser = userPort.findByLogin("testowyAdmin");

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/" + createdUser.getId())
                .then()
                .statusCode(200)
                .body("login", equalTo("testowyAdmin"));
    }

    @Test
    public void testGetAllUsers() throws JsonProcessingException {
        username = "adminek";
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
                .post("")
                .then()
                .statusCode(201);

        RestAssured.given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("")
                .then()
                .statusCode(200)
                .body("[1].login", equalTo(username));
    }
}
