import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import pl.tks.repos.config.MongoProperties;
import pl.tks.repos.entities.user.ClientEnt;
import pl.tks.repos.entities.user.UserEnt;
import pl.tks.repos.repo.UserRepository;

public class UserRepositoryTest extends AbstractMongoDBTest {

    private UserRepository userRepository;
    private MongoProperties mongoProperties;

    @BeforeEach
    public void setup() {
        mongoProperties = new MongoProperties();
        mongoProperties.setUri(mongoDBContainer.getReplicaSetUrl());
        mongoProperties.setDatabase("user_repository_test");
        userRepository = new UserRepository(mongoProperties);
    }

    @AfterEach
    public void teardown() {
        userRepository.getDatabase().getCollection("users").drop();
    }

    @Test
    void addUserTest() {
        ClientEnt user = new ClientEnt(
                new ObjectId(),
                "testLogin",
                "testPassword",
                "Jan",
                "Robak",
                null
        );

        UserEnt savedUser = userRepository.save(user);
        Assertions.assertNotNull(savedUser.getId());
        Assertions.assertEquals("testLogin", savedUser.getLogin());
        Assertions.assertEquals("testPassword", savedUser.getPassword());
        Assertions.assertEquals("Jan", savedUser.getFirstName());
        Assertions.assertEquals("Robak", savedUser.getLastName());
    }

    @Test
    void findByIdTest(){
        ClientEnt user = new ClientEnt(
                new ObjectId(),
                "testLogin",
                "testPassword",
                "Jan",
                "Robak",
                null
        );
        UserEnt savedUser = userRepository.save(user);

        UserEnt receivedUser = userRepository.findById(savedUser.getId().toString());
        Assertions.assertNotNull(receivedUser.getId());
        Assertions.assertEquals("testLogin", receivedUser.getLogin());
        Assertions.assertEquals("testPassword", receivedUser.getPassword());
        Assertions.assertEquals("Jan", receivedUser.getFirstName());
        Assertions.assertEquals("Robak", receivedUser.getLastName());
    }

    @Test
    void findByLoginTest() {
        ClientEnt user = new ClientEnt(
                new ObjectId(),
                "testLogin",
                "testPassword",
                "Jan",
                "Robak",
                null
        );
        UserEnt savedUser = userRepository.save(user);

        UserEnt receivedUser = userRepository.findByLogin(savedUser.getLogin().toString());
        Assertions.assertNotNull(receivedUser.getId());
        Assertions.assertEquals("testLogin", receivedUser.getLogin());
        Assertions.assertEquals("testPassword", receivedUser.getPassword());
        Assertions.assertEquals("Jan", receivedUser.getFirstName());
        Assertions.assertEquals("Robak", receivedUser.getLastName());
    }

    @Test
    void updateUserTest() {
        ClientEnt user = new ClientEnt(
                new ObjectId(),
                "testLogin",
                "testPassword",
                "Jan",
                "Robak",
                null
        );
        UserEnt savedUser = userRepository.save(user);

        UpdateResult result = userRepository.update(savedUser.getId().toString(), "Janek", "Robakowski");

        Assertions.assertNotNull(result);
        UserEnt updatedUser = userRepository.findById(savedUser.getId().toString());
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("Janek", updatedUser.getFirstName());
        Assertions.assertEquals("Robakowski", updatedUser.getLastName());
    }

    @Test
    void activateUserTest() {
        ClientEnt user = new ClientEnt(
                new ObjectId(),
                "testLogin",
                "testPassword",
                "Jan",
                "Robak",
                null
        );
        UserEnt savedUser = userRepository.save(user);
        userRepository.deactivateUser(savedUser.getId().toString());
        UserEnt deactivatedUser = userRepository.findById(savedUser.getId().toString());
        Assertions.assertFalse(deactivatedUser.getActive());

        UpdateResult result = userRepository.activateUser(savedUser.getId().toString());

        Assertions.assertNotNull(result);
        UserEnt updatedUser = userRepository.findById(savedUser.getId().toString());
        Assertions.assertNotNull(updatedUser);
    }

    @Test
    void userExistsTest() {
        ClientEnt user = new ClientEnt(
                new ObjectId(),
                "testLogin",
                "testPassword",
                "Jan",
                "Robak",
                null
        );
        userRepository.save(user);

        boolean exists = userRepository.userExists("testLogin");
        Assertions.assertTrue(exists);

        boolean notExists = userRepository.userExists("testLogin2");
        Assertions.assertFalse(notExists);
    }


//    @AfterEach
//    public void dropCollection() {
//        MongoCollection<UserEnt> userCollection = mongoDBContainer.getDatabase().getCollection("users", UserEnt.class);
//        userCollection.drop();
//    }
}
