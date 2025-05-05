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
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.tks.modelrent.item.Item;
import pl.tks.portsrent.infrastructure.ItemPort;
import pl.tks.restrent.RentRestAdapterApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RentRestAdapterApplication.class)
public class ItemIntegrationTest extends AbstractRestTest {

    @LocalServerPort
    int port;

    @Autowired
    ItemPort itemPort;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api";
        itemPort.deleteAllItems(); // Czyszczenie przed każdym testem
    }

    @AfterEach
    public void cleanUp() {
        itemPort.deleteAllItems(); // Czyszczenie po każdym teście
    }

    private String toJson(Map<String, Object> map) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(map);
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
}