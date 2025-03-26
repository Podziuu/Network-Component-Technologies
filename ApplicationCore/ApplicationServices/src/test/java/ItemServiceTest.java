import dto.ItemDTO;
import dto.MusicDTO;
import exception.InvalidItemTypeException;
import exception.ItemNotFoundException;
import infrastructure.ItemCommandPort;
import model.item.Item;
import model.item.Music;
import model.item.MusicGenre;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import query.ItemQueryPort;
import query.RentQueryPort;
import services.ItemService;

import java.util.Collections;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemCommandPort itemCommandRepository;

    @Mock
    private ItemQueryPort itemQueryRepository;

    @Mock
    private RentQueryPort rentQueryRepository;

    @InjectMocks
    private ItemService itemService;

    private MusicDTO musicDTO;
    private Music updatedMusic;
    private Music music;
    private Music unavailableMusic;
    private final String id = "507f1f77bcf86cd799439011";

    @BeforeEach
    void setUp() {
        musicDTO = new MusicDTO(id, 20, "album", true, MusicGenre.Jazz, false);
        music = new Music(id, 20, "album", true, MusicGenre.Jazz, false);
        unavailableMusic = new Music("507f1f77bcf86cd799439012", 20, "album", false, MusicGenre.Jazz, false);
        updatedMusic = new Music(id, 40, "differentAlbum", true, MusicGenre.POP, true);
    }

    @Test
    void shouldAddMusicItem() {
        when(itemCommandRepository.addItem(any(Music.class))).thenReturn(music);

        Item result = itemService.addItem(music);

        assertNotNull(result);
        assertEquals("album", result.getItemName());
        verify(itemCommandRepository, times(1)).addItem(any(Music.class));
    }

    @Test
    void shouldThrowExceptionForInvalidItemTypeWhenAddingItem() {
        Item invalidItem = new Item();
        invalidItem.setItemType("unknown");

        assertThrows(InvalidItemTypeException.class, () -> itemService.addItem(invalidItem));
    }

    @Test
    void shouldGetItemById() {
        when(itemQueryRepository.getItemById(id)).thenReturn(music);

        Item result = itemService.getItemById(id);

        assertNotNull(result);
        assertEquals("album", result.getItemName());
    }

    @Test
    void shouldThrowExceptionWhenItemNotFound() {
        when(itemQueryRepository.getItemById("4")).thenReturn(null);

        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById("4"));
    }

    @Test
    void shouldRemoveItem() {
        when(itemQueryRepository.getItemById(id)).thenReturn(music);

        itemService.removeItem(id);

        verify(itemCommandRepository, times(1)).removeItem(id);
    }

    @Test
    void shouldSetItemAvailable() {
        when(itemQueryRepository.getItemById("507f1f77bcf86cd799439012")).thenReturn(unavailableMusic);

        itemService.setAvailable("507f1f77bcf86cd799439012");

        assertTrue(unavailableMusic.isAvailable());
        verify(itemCommandRepository, times(1)).updateItem(unavailableMusic);
    }

    @Test
    void shouldThrowExceptionWhenItemNotFoundWhenSetAvailable() {
        when(itemQueryRepository.getItemById("invalid")).thenReturn(null);

        assertThrows(ItemNotFoundException.class, () -> itemService.setAvailable("invalid"));
    }

    @Test
    void shouldUpdateMusicItemSuccessfully() {
        when(itemQueryRepository.getItemById(id)).thenReturn(music);

        itemService.updateItem(id, updatedMusic);

        assertEquals("differentAlbum", music.getItemName());
        assertEquals(40, music.getBasePrice());
        assertEquals(MusicGenre.POP, music.getGenre());
        assertTrue(music.isVinyl());
        verify(itemCommandRepository).updateItem(music);
    }

    @Test
    void shouldGetItemsByBasePrice() {
        when(itemQueryRepository.getItemsByBasePrice(20)).thenReturn(Collections.singletonList(music));

        List<Item> result = itemService.getItemsByBasePrice(20);

        assertNotNull(result);
        verify(itemQueryRepository, times(1)).getItemsByBasePrice(20);
    }

    @Test
    void shouldGetItemsByItemName() {
        when(itemQueryRepository.getItemsByItemName("album")).thenReturn(Collections.singletonList(music));

        List<Item> result = itemService.getItemsByItemName("album");

        assertNotNull(result);
        verify(itemQueryRepository, times(1)).getItemsByItemName("album");
    }

    @Test
    void shouldGetItemsByItemType() {
        when(itemQueryRepository.getItemsByItemType("music")).thenReturn(Collections.singletonList(music));

        List<Item> result = itemService.getItemsByItemType("music");

        assertNotNull(result);
        verify(itemQueryRepository, times(1)).getItemsByItemType("music");
    }

    @Test
    void shouldSetItemUnavailable() {
        when(itemQueryRepository.getItemById("507f1f77bcf86cd799439011")).thenReturn(music);

        itemService.setUnavailable("507f1f77bcf86cd799439011");

        assertFalse(music.isAvailable());
        verify(itemCommandRepository, times(1)).updateItem(music);
    }

    @Test
    void shouldThrowExceptionWhenItemNotFoundWhenSetUnavailable() {
        when(itemQueryRepository.getItemById("invalid")).thenReturn(null);

        assertThrows(ItemNotFoundException.class, () -> itemService.setUnavailable("invalid"));
    }

    @Test
    void shouldGetAllItems() {
        when(itemQueryRepository.getAllItems()).thenReturn(Collections.singletonList(music));

        List<Item> result = itemService.getAllItems();

        assertNotNull(result);
        verify(itemQueryRepository, times(1)).getAllItems();
    }
}
