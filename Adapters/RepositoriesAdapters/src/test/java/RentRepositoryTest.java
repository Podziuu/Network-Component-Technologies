import com.mongodb.client.MongoCollection;
import entities.user.UserEnt;
import entities.RentEnt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import repo.MongoEntity;
import repo.RentRepository;
import repo.UserRepository;

public class RentRepositoryTest {
    private static MongoEntity mongoEntity;
    private static RentRepository rentRepository;

    @BeforeAll
    static void setUp() {
        mongoEntity = new MongoEntity();
        rentRepository = new RentRepository();
    }

    @AfterEach
    public void dropCollection() {
        MongoCollection<RentEnt> rentCollection = mongoEntity.getDatabase().getCollection("rent", RentEnt.class);
        rentCollection.drop();
    }
}
