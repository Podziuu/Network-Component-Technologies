import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import pl.tks.model.user.Admin;
import pl.tks.model.user.Client;
import pl.tks.model.user.ClientType;
import pl.tks.model.user.Manager;
import pl.tks.repos.entities.user.AdminEnt;
import pl.tks.repos.entities.user.ClientEnt;
import pl.tks.repos.entities.user.ManagerEnt;
import pl.tks.repos.mappers.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EntityMapperTest {

//    @Test
//    void movieMapperTest() {
//        Movie movie = new Movie(new ObjectId().toHexString(), 250, "Movie", true, 120, true);
//
//        MovieEnt movieEnt = ItemMapper.toMovieEnt(movie);
//        Movie mappedBackMovie = ItemMapper.toMovie(movieEnt);
//
//        assertNotNull(movieEnt);
//        assertEquals(movie.getBasePrice(), movieEnt.getBasePrice());
//        assertEquals(movie.getItemName(), movieEnt.getItemName());
//        assertEquals(movie.isAvailable(), movieEnt.isAvailable());
//        assertEquals(movie.getMinutes(), movieEnt.getMinutes());
//        assertEquals(movie.isCasette(), movieEnt.isCasette());
//
//        assertNotNull(mappedBackMovie);
//        assertEquals(movie.getBasePrice(), mappedBackMovie.getBasePrice());
//        assertEquals(movie.getItemName(), mappedBackMovie.getItemName());
//        assertEquals(movie.isAvailable(), mappedBackMovie.isAvailable());
//        assertEquals(movie.getMinutes(), mappedBackMovie.getMinutes());
//        assertEquals(movie.isCasette(), mappedBackMovie.isCasette());
//    }
//
//    @Test
//    void musicMapperTest() {
//        Music music = new Music(new ObjectId().toHexString(), 100, "Jazz Album", true, MusicGenre.Jazz, true);
//
//        MusicEnt musicEnt = ItemMapper.toMusicEnt(music);
//        Music mappedBackMusic = ItemMapper.toMusic(musicEnt);
//
//        assertNotNull(musicEnt);
//        assertEquals(music.getBasePrice(), musicEnt.getBasePrice());
//        assertEquals(music.getItemName(), musicEnt.getItemName());
//        assertEquals(music.isAvailable(), musicEnt.isAvailable());
//        assertEquals(music.getGenre(), musicEnt.getGenre());
//        assertEquals(music.isVinyl(), musicEnt.isVinyl());
//
//        assertNotNull(mappedBackMusic);
//        assertEquals(music.getBasePrice(), mappedBackMusic.getBasePrice());
//        assertEquals(music.getItemName(), mappedBackMusic.getItemName());
//        assertEquals(music.isAvailable(), mappedBackMusic.isAvailable());
//        assertEquals(music.getGenre(), mappedBackMusic.getGenre());
//        assertEquals(music.isVinyl(), mappedBackMusic.isVinyl());
//    }
//
//    @Test
//    void comicsMapperTest() {
//        Comics comics = new Comics(new ObjectId().toHexString(), 50, "Marvel", true, 200, "Marvel Studios");
//
//        ComicsEnt comicsEnt = ItemMapper.toComicsEnt(comics);
//        Comics mappedBackComics = ItemMapper.toComics(comicsEnt);
//
//        assertNotNull(comicsEnt);
//        assertEquals(comics.getBasePrice(), comicsEnt.getBasePrice());
//        assertEquals(comics.getItemName(), comicsEnt.getItemName());
//        assertEquals(comics.isAvailable(), comicsEnt.isAvailable());
//        assertEquals(comics.getPageNumber(), comicsEnt.getPageNumber());
//        assertEquals(comics.getPublisher(), comicsEnt.getPublisher());
//
//        assertNotNull(mappedBackComics);
//        assertEquals(comics.getBasePrice(), mappedBackComics.getBasePrice());
//        assertEquals(comics.getItemName(), mappedBackComics.getItemName());
//        assertEquals(comics.isAvailable(), mappedBackComics.isAvailable());
//        assertEquals(comics.getPageNumber(), mappedBackComics.getPageNumber());
//        assertEquals(comics.getPublisher(), mappedBackComics.getPublisher());
//    }

    @Test
    void clientMapperTest() {
        ClientType clientType = new ClientType(5, 10);
        Client client = new Client(new ObjectId().toHexString(), "client123", "password", "Janina", "Dołowska", clientType);

        ClientEnt clientEnt = UserMapper.toClientEnt(client);
        Client mappedBackClient = UserMapper.toClient(clientEnt);

        assertNotNull(clientEnt);
        assertEquals(client.getLogin(), clientEnt.getLogin());
        assertEquals(client.getPassword(), clientEnt.getPassword());
        assertEquals(client.getFirstName(), clientEnt.getFirstName());
        assertEquals(client.getLastName(), clientEnt.getLastName());
        assertEquals(client.getClientType().getMaxArticles(), clientEnt.getClientType().getMaxArticles());
        assertEquals(client.getClientType().getDiscount(), clientEnt.getClientType().getDiscount());

        assertNotNull(mappedBackClient);
        assertEquals(client.getLogin(), mappedBackClient.getLogin());
        assertEquals(client.getPassword(), mappedBackClient.getPassword());
        assertEquals(client.getFirstName(), mappedBackClient.getFirstName());
        assertEquals(client.getLastName(), mappedBackClient.getLastName());
        assertEquals(client.getClientType().getMaxArticles(), mappedBackClient.getClientType().getMaxArticles());
        assertEquals(client.getClientType().getDiscount(), mappedBackClient.getClientType().getDiscount());
    }

    @Test
    void adminMapperTest() {
        Admin admin = new Admin(new ObjectId().toHexString(), "admin123", "password", "Adminek", "Adminowski");

        AdminEnt adminEnt = UserMapper.toAdminEnt(admin);
        Admin mappedBackAdmin = UserMapper.toAdmin(adminEnt);

        assertNotNull(adminEnt);
        assertEquals(admin.getLogin(), adminEnt.getLogin());
        assertEquals(admin.getPassword(), adminEnt.getPassword());
        assertEquals(admin.getFirstName(), adminEnt.getFirstName());
        assertEquals(admin.getLastName(), adminEnt.getLastName());

        assertNotNull(mappedBackAdmin);
        assertEquals(admin.getLogin(), mappedBackAdmin.getLogin());
        assertEquals(admin.getPassword(), mappedBackAdmin.getPassword());
        assertEquals(admin.getFirstName(), mappedBackAdmin.getFirstName());
        assertEquals(admin.getLastName(), mappedBackAdmin.getLastName());
    }

    @Test
    void managerMapperTest() {
        Manager manager = new Manager(new ObjectId().toHexString(), "manager123", "password", "Janusz", "Brązowy");

        ManagerEnt managerEnt = UserMapper.toManagerEnt(manager);
        Manager mappedBackManager = UserMapper.toManager(managerEnt);

        assertNotNull(managerEnt);
        assertEquals(manager.getLogin(), managerEnt.getLogin());
        assertEquals(manager.getPassword(), managerEnt.getPassword());
        assertEquals(manager.getFirstName(), managerEnt.getFirstName());
        assertEquals(manager.getLastName(), managerEnt.getLastName());

        assertNotNull(mappedBackManager);
        assertEquals(manager.getLogin(), mappedBackManager.getLogin());
        assertEquals(manager.getPassword(), mappedBackManager.getPassword());
        assertEquals(manager.getFirstName(), mappedBackManager.getFirstName());
        assertEquals(manager.getLastName(), mappedBackManager.getLastName());
    }

//    @Test
//    void rentMapperTest() {
//        ClientType clientType = new ClientType(5, 10);
//
//        Client client = new Client(new ObjectId().toHexString(), "client123", "password", "Jan", "Robak", clientType);
//        ClientEnt clientEnt = UserMapper.toClientEnt(client);
//        Comics comics = new Comics(new ObjectId().toHexString(), 50, "Marvel", true, 200, "Marvel Studios");
//        ComicsEnt comicsEnt = ItemMapper.toComicsEnt(comics);
//
//        Rent rent1 = new Rent(LocalDateTime.now(), 50, client, comics);
//        Rent rent2 = new Rent(LocalDateTime.now().plusDays(1), 100, client, comics);
//
//        RentEnt rentEnt1 = new RentEnt(LocalDateTime.now(), 50, clientEnt, comicsEnt);
//        RentEnt rentEnt2 = new RentEnt(LocalDateTime.now().plusDays(1), 100, clientEnt, comicsEnt);
//
//        RentEnt mappedRentEnt = RentMapper.toEnt(rent1);
//        Rent mappedRent = RentMapper.toRent(rentEnt1);
//
//        List<RentEnt> rentEntList = RentMapper.toEntList(List.of(rent1, rent2));
//        List<Rent> rentList = RentMapper.toRentList(List.of(rentEnt1, rentEnt2));
//
//        assertNotNull(mappedRentEnt);
//        assertEquals(rent1.getBeginTime(), mappedRentEnt.getBeginTime());
//        assertEquals(rent1.getRentCost(), mappedRentEnt.getRentCost());
//        assertNotNull(mappedRentEnt.getClient());
//
//        assertNotNull(mappedRent);
//        assertEquals(rentEnt1.getBeginTime(), mappedRent.getBeginTime());
//        assertEquals(rentEnt1.getRentCost(), mappedRent.getRentCost());
//
//        assertNotNull(rentEntList);
//        assertEquals(2, rentEntList.size());
//        assertEquals(rent1.getBeginTime(), rentEntList.get(0).getBeginTime());
//        assertEquals(rent2.getRentCost(), rentEntList.get(1).getRentCost());
//
//        assertNotNull(rentList);
//        assertEquals(2, rentList.size());
//        assertEquals(rentEnt1.getBeginTime(), rentList.get(0).getBeginTime());
//        assertEquals(rentEnt2.getRentCost(), rentList.get(1).getRentCost());
//    }
}
