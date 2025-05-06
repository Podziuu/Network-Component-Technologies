import jakarta.xml.bind.JAXBElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tks.modelrent.item.Item;
import pl.tks.modelrent.item.MusicGenre;
import pl.tks.servicerent.services.ItemService;
import pl.tks.soap.endpoint.ItemEndpoint;
import pl.tks.items.ObjectFactory;
import generated.*;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ItemEndpointTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemEndpoint itemEndpoint;

    private final ObjectFactory factory = new ObjectFactory();

    @Test
    void getItems_returnsAllItems() {
        pl.tks.modelrent.item.Comics comics = new pl.tks.modelrent.item.Comics();
        comics.setBasePrice(32);
        comics.setItemName("Spiderman");
        comics.setAvailable(true);
        comics.setItemType("Comics");
        comics.setPageNumber(72);
        comics.setPublisher("Marvel");
        pl.tks.modelrent.item.Music music = new pl.tks.modelrent.item.Music();
        music.setBasePrice(25);
        music.setItemName("Music");
        music.setAvailable(true);
        music.setItemType("Music");
        music.setGenre(MusicGenre.valueOf("Metal"));
        music.setVinyl(false);

        List<Item> items = new ArrayList<>();
        items.add(comics);
        items.add(music);

        when(itemService.getAllItems()).thenReturn(items);

        GetItemsRequest request = new GetItemsRequest();
        JAXBElement<GetItemsRequest> jaxbRequest = new JAXBElement<>(
                new QName("http://tks.pl/items", "getItemsRequest"),
                GetItemsRequest.class, request);

        JAXBElement<GetItemsResponse> response = itemEndpoint.getItems(jaxbRequest);

        assertEquals(2, response.getValue().getItems().size());
        assertEquals("Spiderman", response.getValue().getItems().get(0).getItemName());
        assertEquals("Music", response.getValue().getItems().get(1).getItemName());
        verify(itemService, times(1)).getAllItems();
    }

    @Test
    void addComics_addsAndReturnsComics() {
        AddComicsRequest request = new AddComicsRequest();
        request.setItemName("Batman");
        request.setBasePrice(97);
        request.setAvailable(true);
        request.setItemType("COMICS");
        request.setPageNumber(200);
        request.setPublisher("DC");

        JAXBElement<AddComicsRequest> jaxbRequest = factory.createAddComicsRequest(request);

        pl.tks.modelrent.item.Comics addedItem = new pl.tks.modelrent.item.Comics();
        addedItem.setBasePrice(97);
        addedItem.setItemName("Batman");
        addedItem.setAvailable(true);
        addedItem.setItemType("COMICS");
        addedItem.setPageNumber(200);
        addedItem.setPublisher("DC");
        when(itemService.addItem(any())).thenReturn(addedItem);

        JAXBElement<AddComicsResponse> response = itemEndpoint.addComics(jaxbRequest);

        assertEquals("Batman", response.getValue().getItem().getItemName());
        assertEquals(97, response.getValue().getItem().getBasePrice());
        assertTrue(response.getValue().getItem().isAvailable());
        assertEquals(200, response.getValue().getItem().getPageNumber());
        assertEquals("DC", response.getValue().getItem().getPublisher());
        verify(itemService, times(1)).addItem(any());
    }

    @Test
    void addMovie_addsAndReturnsMovie() {
        AddMovieRequest request = new AddMovieRequest();
        request.setItemName("Matrix");
        request.setBasePrice(80);
        request.setAvailable(true);
        request.setItemType("MOVIE");
        request.setMinutes(120);
        request.setCasette(false);

        JAXBElement<AddMovieRequest> jaxbRequest = factory.createAddMovieRequest(request);

        pl.tks.modelrent.item.Movie addedItem = new pl.tks.modelrent.item.Movie();
        addedItem.setBasePrice(80);
        addedItem.setItemName("Matrix");
        addedItem.setAvailable(true);
        addedItem.setItemType("Movie");
        addedItem.setMinutes(120);
        addedItem.setCasette(false);
        when(itemService.addItem(any())).thenReturn(addedItem);

        JAXBElement<AddMovieResponse> response = itemEndpoint.addMovie(jaxbRequest);

        assertEquals("Matrix", response.getValue().getItem().getItemName());
        assertEquals(80, response.getValue().getItem().getBasePrice());
        assertTrue(response.getValue().getItem().isAvailable());
        assertEquals(120, response.getValue().getItem().getMinutes());
        assertFalse(response.getValue().getItem().isCasette());
        verify(itemService, times(1)).addItem(any());
    }

    @Test
    void addMusic_addsAndReturnsMusic() {
        AddMusicRequest request = new AddMusicRequest();
        request.setItemName("Music");
        request.setBasePrice(25);
        request.setAvailable(true);
        request.setItemType("Music");
        request.setGenre(String.valueOf(MusicGenre.valueOf("Metal")));
        request.setVinyl(true);

        JAXBElement<AddMusicRequest> jaxbRequest = factory.createAddMusicRequest(request);

        pl.tks.modelrent.item.Music addedItem = new pl.tks.modelrent.item.Music();
        addedItem.setBasePrice(25);
        addedItem.setItemName("Music");
        addedItem.setAvailable(true);
        addedItem.setItemType("Music");
        addedItem.setGenre(MusicGenre.valueOf("Metal"));
        addedItem.setVinyl(true);
        when(itemService.addItem(any())).thenReturn(addedItem);

        JAXBElement<AddMusicResponse> response = itemEndpoint.addMusic(jaxbRequest);

        assertEquals("Music", response.getValue().getItem().getItemName());
        assertEquals(25, response.getValue().getItem().getBasePrice());
        assertTrue(response.getValue().getItem().isAvailable());
        assertTrue(response.getValue().getItem().isVinyl());
        verify(itemService, times(1)).addItem(any());
    }
}
