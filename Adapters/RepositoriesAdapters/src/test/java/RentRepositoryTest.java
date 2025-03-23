import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entities.item.ItemEnt;
import entities.item.MovieEnt;
import entities.user.ClientEnt;
import entities.user.UserEnt;
import entities.RentEnt;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import repo.ItemRepository;
import repo.MongoEntity;
import repo.RentRepository;
import repo.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

public class RentRepositoryTest {
    private static MongoEntity mongoEntity;
    private static RentRepository rentRepository;
    private static UserRepository userRepository;
    private static ItemRepository itemRepository;
    private static MongoDatabase database;
    private static MongoCollection<RentEnt> rentCollection;
    private static MongoCollection<ItemEnt> itemCollection;
    private static MongoCollection<UserEnt> userCollection;

    @BeforeAll
    static void setUp() {
        mongoEntity = new MongoEntity();
        database = mongoEntity.getDatabase();

        itemCollection = database.getCollection("items", ItemEnt.class);
        rentCollection = database.getCollection("rents", RentEnt.class);
        userCollection = database.getCollection("users", UserEnt.class);

        rentRepository = new RentRepository();
        userRepository = new UserRepository();
        itemRepository = new ItemRepository();
    }

    @BeforeEach
    public void clearCollections() {
        itemCollection.drop();
        userCollection.drop();
        rentCollection.drop();
    }


    @Test
    public void rentItemTest() {
        MovieEnt movie = new MovieEnt(null, 250, "Movie", 120, true);
        ObjectId itemId = itemRepository.addItem(movie).getId();

        ClientEnt user = new ClientEnt(
                new ObjectId(),
                "testLogin1",
                "testPassword",
                "Jan",
                "Robak",
                null
        );

        UserEnt savedUser = userRepository.save(user);

        RentEnt rent = new RentEnt();
        rent.setItem(movie);
        rent.setClient(user);
        rent.setBeginTime(LocalDateTime.now());

        RentEnt rentedItem = rentRepository.addRent(rent);

        Assertions.assertNotNull(rentedItem.getId());
        Assertions.assertEquals(rentedItem.getItem().getId(), itemId);
        Assertions.assertEquals(rentedItem.getClient().getId(), savedUser.getId());
    }

    @Test
    public void rentItemWithEndTimeTest() {
        MovieEnt movie = new MovieEnt(null, 250, "Movie", 120, true);
        ObjectId itemId = itemRepository.addItem(movie).getId();

        ClientEnt client = new ClientEnt(
                new ObjectId(),
                "testLogin2",
                "testPassword2",
                "Anna",
                "Kowalska",
                null
        );
        UserEnt savedUser = userRepository.save(client);

        RentEnt rent = new RentEnt();
        rent.setItem(movie);
        rent.setClient(client);
        rent.setBeginTime(LocalDateTime.now());
        rent.setEndTime(LocalDateTime.now().plusDays(5));

        RentEnt rentedItem = rentRepository.addRent(rent);

        Assertions.assertNotNull(rentedItem.getEndTime());
        Assertions.assertEquals(rentedItem.getItem().getId(), itemId);
        Assertions.assertEquals(rentedItem.getClient().getId(), savedUser.getId());
    }

    @Test
    public void rentItemWithNoEndTimeTest() {
        MovieEnt movie = new MovieEnt(null, 250, "Movie", 120, true);
        ObjectId itemId = itemRepository.addItem(movie).getId();

        ClientEnt client = new ClientEnt(
                new ObjectId(),
                "testLogin3",
                "testPassword3",
                "Ewa",
                "Nowak",
                null
        );
        UserEnt savedUser = userRepository.save(client);

        RentEnt rent = new RentEnt();
        rent.setItem(movie);
        rent.setClient(client);
        rent.setBeginTime(LocalDateTime.now());
        rent.setEndTime(null);

        RentEnt rentedItem = rentRepository.addRent(rent);

        Assertions.assertNull(rentedItem.getEndTime());
        Assertions.assertEquals(rentedItem.getItem().getId(), itemId);
        Assertions.assertEquals(rentedItem.getClient().getId(), savedUser.getId());
    }

    @Test
    public void updateRentEndTimeTest() {
        MovieEnt movie = new MovieEnt(null, 250, "Movie", 120, true);
        ObjectId itemId = itemRepository.addItem(movie).getId();

        ClientEnt client = new ClientEnt(
                new ObjectId(),
                "testLogin4",
                "testPassword4",
                "Kamil",
                "Wojcik",
                null
        );
        UserEnt savedUser = userRepository.save(client);

        RentEnt rent = new RentEnt();
        rent.setItem(movie);
        rent.setClient(client);
        rent.setBeginTime(LocalDateTime.now());

        RentEnt rentedItem = rentRepository.addRent(rent);

        rentedItem.setEndTime(LocalDateTime.now().plusDays(3));
        rentRepository.updateRent(rentedItem);

        RentEnt updatedRent = rentRepository.getRent(rentedItem.getId().toHexString());
        Assertions.assertNotNull(updatedRent);
        Assertions.assertNotNull(updatedRent.getEndTime());
        Assertions.assertTrue(updatedRent.getEndTime().isAfter(rentedItem.getBeginTime()));
    }

    @Test
    public void findActiveRentsByItemIdTest() {
        MovieEnt movie = new MovieEnt(null, 250, "Movie", 120, true);
        ObjectId itemId = itemRepository.addItem(movie).getId();

        ClientEnt client = new ClientEnt(
                new ObjectId(),
                "testLogin5",
                "testPassword5",
                "Tomasz",
                "Zielinski",
                null
        );
        UserEnt savedUser = userRepository.save(client);

        RentEnt rent = new RentEnt();
        rent.setItem(movie);
        rent.setClient(client);
        rent.setBeginTime(LocalDateTime.now());
        rent.setEndTime(null);

        rentRepository.addRent(rent);

        List<RentEnt> activeRents = rentRepository.findActiveRentsByItemId(itemId.toHexString());

        Assertions.assertTrue(activeRents.size() > 0);
        Assertions.assertNull(activeRents.get(0).getEndTime());
        Assertions.assertEquals(activeRents.get(0).getItem().getId(), itemId);
    }

    @Test
    public void findInactiveRentsByItemIdTest() {
        MovieEnt movie = new MovieEnt(null, 250, "Movie", 120, true);
        ObjectId itemId = itemRepository.addItem(movie).getId();

        ClientEnt client = new ClientEnt(
                new ObjectId(),
                "testLogin6",
                "testPassword6",
                "Magda",
                "Jankowska",
                null
        );
        UserEnt savedUser = userRepository.save(client);

        RentEnt rent = new RentEnt();
        rent.setItem(movie);
        rent.setClient(client);
        rent.setBeginTime(LocalDateTime.now());
        rent.setEndTime(LocalDateTime.now().plusDays(3));

        rentRepository.addRent(rent);

        List<RentEnt> inactiveRents = rentRepository.findInactiveRentsByItemId(itemId.toHexString());

        Assertions.assertTrue(inactiveRents.size() > 0);
        Assertions.assertNotNull(inactiveRents.get(0).getEndTime());
        Assertions.assertEquals(inactiveRents.get(0).getItem().getId(), itemId);
    }

    @Test
    public void findActiveRentsByClientIdTest() {
        MovieEnt movie = new MovieEnt(null, 250, "Movie", 120, true);
        ObjectId itemId = itemRepository.addItem(movie).getId();

        ClientEnt client = new ClientEnt(
                new ObjectId(),
                "testLogin7",
                "testPassword7",
                "Adam",
                "Pawlak",
                null
        );
        UserEnt savedUser = userRepository.save(client);

        RentEnt rent = new RentEnt();
        rent.setItem(movie);
        rent.setClient(client);
        rent.setBeginTime(LocalDateTime.now());
        rent.setEndTime(null);

        rentRepository.addRent(rent);

        List<RentEnt> activeRents = rentRepository.findActiveRentsByClientId(savedUser.getId().toHexString());

        Assertions.assertTrue(activeRents.size() > 0);
        Assertions.assertNull(activeRents.get(0).getEndTime());
        Assertions.assertEquals(activeRents.get(0).getClient().getId(), savedUser.getId());
    }

    @Test
    public void findInactiveRentsByClientIdTest() {
        MovieEnt movie = new MovieEnt(null, 250, "Movie", 120, true);
        ObjectId itemId = itemRepository.addItem(movie).getId();

        ClientEnt client = new ClientEnt(
                new ObjectId(),
                "testLogin8",
                "testPassword8",
                "Katarzyna",
                "Wróbel",
                null
        );
        UserEnt savedUser = userRepository.save(client);

        RentEnt rent = new RentEnt();
        rent.setItem(movie);
        rent.setClient(client);
        rent.setBeginTime(LocalDateTime.now());
        rent.setEndTime(LocalDateTime.now().plusDays(2));

        rentRepository.addRent(rent);

        List<RentEnt> inactiveRents = rentRepository.findInactiveRentsByClientId(savedUser.getId().toHexString());

        Assertions.assertTrue(inactiveRents.size() > 0);
        Assertions.assertNotNull(inactiveRents.get(0).getEndTime());
        Assertions.assertEquals(inactiveRents.get(0).getClient().getId(), savedUser.getId());
    }


    @AfterEach
    public void dropCollection() {
        MongoCollection<RentEnt> rentCollection = mongoEntity.getDatabase().getCollection("rents", RentEnt.class);
        MongoCollection<ItemEnt> itemCollection = mongoEntity.getDatabase().getCollection("items", ItemEnt.class);
        MongoCollection<UserEnt> userCollection = mongoEntity.getDatabase().getCollection("users", UserEnt.class);
        itemCollection.drop();
        userCollection.drop();
        rentCollection.drop();
    }
}
