import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entities.item.ComicsEnt;
import entities.item.ItemEnt;
import entities.item.MovieEnt;
import entities.item.MusicEnt;
import model.item.MusicGenre;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import repo.ItemRepository;

import org.junit.jupiter.api.Test;


import repo.MongoEntity;


public class ItemRepositoryTest {
    private static MongoEntity mongoEntity;
    private static MongoDatabase database;
    private static MongoCollection<ItemEnt> itemCollection;
    private static ItemRepository itemRepository;

    @BeforeAll
    static void setUp() {
        mongoEntity = new MongoEntity();
        database = mongoEntity.getDatabase();
        itemCollection = database.getCollection("items", ItemEnt.class);
        itemRepository = new ItemRepository();
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

//    @Test
//    void removeItemInvalidTest() {
//        Assertions.assertThrows(NullPointerException.class, () -> {
//            itemRepository.removeItem(new ObjectId());
//        });
//    } //d

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

//    @Test
//    void updateItemInvalidTest() {
//        //obiekt bez id
//        ItemEnt item = new ItemEnt();
//        item.setItemName("invalid");
//        Assertions.assertThrows(NullPointerException.class, () -> {
//           itemRepository.updateItem(item);
//        });
//    } //d

    @Test
    void setUnavailableTest() {
        ComicsEnt comics = new ComicsEnt(null, 250, "Movie", 120,
                "Siemaszko");
        ObjectId itemId = itemRepository.addItem(comics).getId();
        comics.setAvailable(false);
        itemRepository.updateItem(comics);
        ItemEnt itemEnt = itemRepository.getItemById(itemId.toString());
        Assertions.assertFalse(itemEnt.isAvailable());
    } //d

    @AfterAll
    static void tearDown() throws Exception {
        mongoEntity.close();
    }
}
