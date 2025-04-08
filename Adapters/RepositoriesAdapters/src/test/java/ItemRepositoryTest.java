import entities.item.ComicsEnt;
import entities.item.ItemEnt;
import entities.item.MovieEnt;
import entities.item.MusicEnt;
import model.item.MusicGenre;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import repo.ItemRepository;
import repo.MongoEntity;

//@SpringBootTest
@Import(TestMongoConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemRepositoryTest {
//    private static MongoEntity mongoEntity;
//    private static MongoDatabase database;
//    private static MongoCollection<ItemEnt> itemCollection;
    private static ItemRepository itemRepository;

    @Container
    static final MongoDBContainer mongoContainer = new MongoDBContainer("mongo:6.0")
            .withReuse(true)
            .withCommand("--replSet", "rs0");

    @BeforeAll
    static void setUp() {
        mongoContainer.start();
        String uri = mongoContainer.getReplicaSetUrl();
//        mongoEntity = new MongoEntity();
//        database = mongoEntity.getDatabase();
//        itemCollection = database.getCollection("items", ItemEnt.class);
        itemRepository = new ItemRepository(new MongoEntity(uri));
    }

    @Test
    void addMovieTest() {
        MovieEnt movie = new MovieEnt(null, 250, "Movie", 120, true);
        ObjectId itemId = itemRepository.addItem(movie).getId();
        Assertions.assertNotNull(itemId);
        Assertions.assertEquals(itemId, movie.getId());
    }

    @Test
    void addMusicTest() {
        MusicEnt music = new MusicEnt(null, 250, "Movie", MusicGenre.Jazz, true);
        ObjectId itemId = itemRepository.addItem(music).getId();
        Assertions.assertNotNull(itemId);
        Assertions.assertEquals(itemId, music.getId());
    }


    @Test
    void addComicsTest() {
        ComicsEnt comics = new ComicsEnt(null, 250, "Movie", 120,
                "Siemaszko");
        ObjectId itemId = itemRepository.addItem(comics).getId();
        Assertions.assertNotNull(itemId);
        Assertions.assertEquals(itemId, comics.getId());
    }

    @Test
    void removeItemTest() {
        ComicsEnt comics = new ComicsEnt(null, 250, "Movie", 120,
                "Siemaszko");
        ObjectId itemId = itemRepository.addItem(comics).getId();
        itemRepository.removeItem(itemId);
        Assertions.assertNull(itemRepository.getItemById(itemId.toString()));
    }

    @Test
    void updateItemTest() {
        ComicsEnt comics = new ComicsEnt(null, 250, "Movie", 120,
                "Siemaszko");
        ObjectId itemId = itemRepository.addItem(comics).getId();

        comics.setItemName("new name");

        itemRepository.updateItem(comics);
        ItemEnt updatedItemEnt = itemRepository.getItemById(itemId.toString());
        Assertions.assertNotNull(updatedItemEnt);
        Assertions.assertEquals(updatedItemEnt.getItemName(), comics.getItemName());
        Assertions.assertEquals("new name", comics.getItemName());
    }

    @Test
    void setUnavailableTest() {
        ComicsEnt comics = new ComicsEnt(null, 250, "Movie", 120,
                "Siemaszko");
        ObjectId itemId = itemRepository.addItem(comics).getId();
        comics.setAvailable(false);
        itemRepository.updateItem(comics);
        ItemEnt itemEnt = itemRepository.getItemById(itemId.toString());
        Assertions.assertFalse(itemEnt.isAvailable());
    }

//    @AfterAll
//    static void tearDown() throws Exception {
//        mongoEntity.close();
//    }
//    @AfterEach
//    public void dropCollection() {
//        MongoCollection<RentEnt> rentCollection = mongoEntity.getDatabase().getCollection("rent", RentEnt.class);
//        MongoCollection<ItemEnt> itemCollection = mongoEntity.getDatabase().getCollection("item", ItemEnt.class);
//        MongoCollection<UserEnt> userCollection = mongoEntity.getDatabase().getCollection("user", UserEnt.class);
//        itemCollection.drop();
//        userCollection.drop();
//        rentCollection.drop();
//    }
}
