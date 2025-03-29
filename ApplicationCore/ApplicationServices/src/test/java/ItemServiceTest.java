import exception.InvalidItemTypeException;
import exception.ItemAlreadyRentedException;
import exception.ItemNotFoundException;
import infrastructure.ItemPort;
import infrastructure.RentPort;
import model.Rent;
import model.item.Item;
import model.item.Music;
import model.item.MusicGenre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ItemService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemPort itemPort;

    @Mock
    private RentPort rentPort;

    @InjectMocks
    private ItemService itemService;

    private Music updatedMusic;
    private Music music;
    private Music unavailableMusic;
    private final String id = "507f1f77bcf86cd799439011";

    @BeforeEach
    void setUp() {
        music = new Music(id, 20, "album", true, MusicGenre.Jazz, false);
        unavailableMusic = new Music("507f1f77bcf86cd799439012", 20, "album", false, MusicGenre.Jazz, false);
        updatedMusic = new Music(id, 40, "differentAlbum", true, MusicGenre.POP, true);
    }

    @Test
    void shouldAddMusicItem() {
        when(itemPort.addItem(any(Music.class))).thenReturn(music);

        Item result = itemService.addItem(music);

        assertNotNull(result);
        assertEquals("album", result.getItemName());
        verify(itemPort, times(1)).addItem(any(Music.class));
    }

    @Test
    void shouldThrowExceptionForInvalidItemTypeWhenAddingItem() {
        Item invalidItem = new Item();

        assertThrows(InvalidItemTypeException.class, () -> itemService.addItem(invalidItem));
    }

    @Test
    void shouldGetItemById() {
        when(itemPort.getItemById(id)).thenReturn(music);

        Item result = itemService.getItemById(id);

        assertNotNull(result);
        assertEquals("album", result.getItemName());
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        when(itemPort.getItemById("4")).thenReturn(null);

        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById("4"));
    }

    @Test
    void shouldRemoveItem() {
        when(itemPort.getItemById(id)).thenReturn(music);
        when(rentPort.findActiveRentsByItemId(id)).thenReturn(Collections.emptyList());

        itemService.removeItem(id);

        verify(itemPort, times(1)).removeItem(id);
    }

    @Test
    void shouldThrowExceptionWhenItemIsRented() {
        when(itemPort.getItemById(id)).thenReturn(music);
        when(rentPort.findActiveRentsByItemId(id)).thenReturn(List.of(new Rent()));

        assertThrows(ItemAlreadyRentedException.class, () -> itemService.removeItem(id));
        verify(itemPort, never()).removeItem(id);
    }

    @Test
    void shouldSetItemAvailable() {
        when(itemPort.getItemById("507f1f77bcf86cd799439012")).thenReturn(unavailableMusic);

        itemService.setAvailable("507f1f77bcf86cd799439012");

        assertTrue(unavailableMusic.isAvailable());
        verify(itemPort, times(1)).updateItem(unavailableMusic);
    }

    @Test
    void shouldThrowExceptionWhenItemNotFoundWhenSetAvailable() {
        when(itemPort.getItemById("invalid")).thenReturn(null);

        assertThrows(ItemNotFoundException.class, () -> itemService.setAvailable("invalid"));
    }

    @Test
    void shouldUpdateMusicItemSuccessfully() {
        when(itemPort.getItemById(id)).thenReturn(music);

        itemService.updateItem(id, updatedMusic);

        assertEquals("differentAlbum", music.getItemName());
        assertEquals(40, music.getBasePrice());
        assertEquals(MusicGenre.POP, music.getGenre());
        assertTrue(music.isVinyl());
        verify(itemPort).updateItem(music);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentItem() {
        when(itemPort.getItemById(id)).thenReturn(null);

        assertThrows(ItemNotFoundException.class, () -> itemService.updateItem(id, updatedMusic));
        verify(itemPort, never()).updateItem(any());
    }

    @Test
    void shouldGetItemsByBasePrice() {
        when(itemPort.getItemsByBasePrice(20)).thenReturn(Collections.singletonList(music));

        List<Item> result = itemService.getItemsByBasePrice(20);

        assertNotNull(result);
        verify(itemPort, times(1)).getItemsByBasePrice(20);
    }

    @Test
    void shouldThrowExceptionWhenNoItemsFoundByBasePrice() {
        when(itemPort.getItemsByBasePrice(30)).thenReturn(Collections.emptyList());

        assertThrows(ItemNotFoundException.class, () -> itemService.getItemsByBasePrice(30));
    }

    @Test
    void shouldGetItemsByItemName() {
        when(itemPort.getItemsByItemName("album")).thenReturn(Collections.singletonList(music));

        List<Item> result = itemService.getItemsByItemName("album");

        assertNotNull(result);
        verify(itemPort, times(1)).getItemsByItemName("album");
    }

    @Test
    void shouldThrowExceptionWhenNoItemsFoundByItemName() {
        when(itemPort.getItemsByItemName("unknown")).thenReturn(Collections.emptyList());

        assertThrows(ItemNotFoundException.class, () -> itemService.getItemsByItemName("unknown"));
    }

    @Test
    void shouldGetItemsByItemType() {
        when(itemPort.getItemsByItemType("music")).thenReturn(Collections.singletonList(music));

        List<Item> result = itemService.getItemsByItemType("music");

        assertNotNull(result);
        verify(itemPort, times(1)).getItemsByItemType("music");
    }

    @Test
    void shouldThrowExceptionWhenNoItemsFoundByItemType() {
        when(itemPort.getItemsByItemType("unknown")).thenReturn(Collections.emptyList());

        assertThrows(ItemNotFoundException.class, () -> itemService.getItemsByItemType("unknown"));
    }

    @Test
    void shouldSetItemUnavailable() {
        when(itemPort.getItemById("507f1f77bcf86cd799439011")).thenReturn(music);

        itemService.setUnavailable("507f1f77bcf86cd799439011");

        assertFalse(music.isAvailable());
        verify(itemPort, times(1)).updateItem(music);
    }

    @Test
    void shouldThrowExceptionWhenItemNotFoundWhenSetUnavailable() {
        when(itemPort.getItemById("invalid")).thenReturn(null);

        assertThrows(ItemNotFoundException.class, () -> itemService.setUnavailable("invalid"));
    }

    @Test
    void shouldGetAllItems() {
        when(itemPort.getAllItems()).thenReturn(Collections.singletonList(music));

        List<Item> result = itemService.getAllItems();

        assertNotNull(result);
        verify(itemPort, times(1)).getAllItems();
    }

    @Test
    void shouldThrowExceptionWhenNoItemsFound() {
        when(itemPort.getAllItems()).thenReturn(Collections.emptyList());

        assertThrows(ItemNotFoundException.class, () -> itemService.getAllItems());
    }
}
