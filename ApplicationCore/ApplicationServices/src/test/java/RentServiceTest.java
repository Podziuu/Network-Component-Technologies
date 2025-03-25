import dto.MusicDTO;
import dto.RentDTO;
import exception.*;
import infrastructure.RentCommandPort;
import infrastructure.UserCommandPort;
import model.Rent;
import model.item.Music;
import model.item.MusicGenre;
import model.user.Client;
import model.user.ClientType;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import query.RentQueryPort;
import query.UserQueryPort;
import services.ItemService;
import services.RentService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentServiceTest {
    @Mock
    private RentCommandPort rentCommandPort;
    @Mock
    private RentQueryPort rentQueryPort;
    @Mock
    private UserCommandPort userCommandPort;
    @Mock
    private UserQueryPort userQueryPort;
    @Mock
    private ItemService itemService;

    @InjectMocks
    private RentService rentService;

    private RentDTO rentDTO;
    private Client client;
    private MusicDTO musicDTO;
    private Music music;
    private Rent rent;
    private Rent rent2;
    private final String id = "507f1f77bcf86cd799439011";

    @BeforeEach
    void setUp() {
        rentDTO = new RentDTO(300, "32", "32");
        client = new Client("123", "JDoe", "password", "Joe", "Doe", ClientType.createNoMembership());
        musicDTO = new MusicDTO(id, 20, "album", true, MusicGenre.Jazz, false);
        music = new Music(id, 20, "album", true, MusicGenre.Jazz, false);
        rent = new Rent("rentId", rentDTO.getBeginTime(), rentDTO.getRentCost(), client, music);
        rent2 = new Rent("rentId2", LocalDateTime.now(), rentDTO.getRentCost(), client, music);
    }

    @Test
    void shouldRentItemSuccessfully() {
        when(userQueryPort.getById(rentDTO.getClientId())).thenReturn(client);
        when(itemService.getItemById(rentDTO.getItemId())).thenReturn(musicDTO);
        when(rentCommandPort.add(any(Rent.class))).thenReturn(rent2);

        RentDTO result = rentService.rentItem(rentDTO);

        assertNotNull(result);
        assertNotNull(result.getBeginTime());
        assertNull(result.getEndTime());
        assertEquals(result.getRentCost(), rent.getRentCost());
        verify(itemService).setUnavailable(any(ObjectId.class));
        verify(rentCommandPort).add(any(Rent.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundWhileRenting() {
        when(userQueryPort.getById(rentDTO.getClientId())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> rentService.rentItem(rentDTO));
    }

    @Test
    void shouldThrowExceptionWhenItemNotFoundWhileRenting() {
        when(userQueryPort.getById(rentDTO.getClientId())).thenReturn(client);
        when(itemService.getItemById(rentDTO.getItemId())).thenReturn(null);

        assertThrows(ItemNotFoundException.class, () -> rentService.rentItem(rentDTO));
    }

    @Test
    void shouldThrowErrorWhenItemAlreadyRenterWhileRenting() {
        musicDTO.setAvailable(false);
        when(userQueryPort.getById(rentDTO.getClientId())).thenReturn(client);
        when(itemService.getItemById(rentDTO.getItemId())).thenReturn(musicDTO);

        assertThrows(ItemAlreadyRentedException.class, () -> rentService.rentItem(rentDTO));
    }

    @Test
    void shouldGetRentByIdSuccessfully() {
        when(rentQueryPort.getById("rentId")).thenReturn(rent);

        RentDTO result = rentService.getRentById("rentId");

        assertNotNull(result);
        assertEquals(result.getRentCost(), rent.getRentCost());
    }

    @Test
    void shouldThrowExceptionWhenRentNotFoundById() {
        when(rentQueryPort.getById("rentId")).thenReturn(null);

        assertThrows(RentNotFoundException.class, () -> rentService.getRentById("rentId"));
    }

    @Test
    void shouldReturnRentSuccessfully() {
        when(rentQueryPort.getById("rentId")).thenReturn(rent);
        doNothing().when(rentCommandPort).update(any(Rent.class));

        rentService.returnRent("rentId");

        verify(itemService).setAvailable(any(ObjectId.class));
        verify(rentCommandPort).update(any(Rent.class));
    }

    @Test
    void shouldThrowExceptionWhenRentNotFoundWhileReturning() {
        when(rentQueryPort.getById("rentId")).thenReturn(null);

        assertThrows(RentOperationException.class, () -> rentService.returnRent("rentId"));
    }

    @Test
    void shouldGetActiveRentSuccessfully() {
        when(rentQueryPort.getActiveRents()).thenReturn(Collections.singletonList(rent));

        List<RentDTO> result = rentService.getActiveRents();

        assertNotNull(result);
        verify(rentQueryPort).getActiveRents();
    }

    @Test
    void shouldThrowExceptionWhenNoRentsFoundWhileGetActiveRents() {
        when(rentQueryPort.getActiveRents()).thenReturn(null);

        assertThrows(RentNotFoundException.class, () -> rentService.getActiveRents());
    }

    @Test
    void shouldGetInactiveRentSuccessfully() {
        when(rentQueryPort.getInactiveRents()).thenReturn(Collections.singletonList(rent));

        List<RentDTO> result = rentService.getInactiveRents();

        assertNotNull(result);
        verify(rentQueryPort).getInactiveRents();
    }

    @Test
    void shouldThrowExceptionWhenNoRentsFoundWhileGetInactiveRents() {
        when(rentQueryPort.getInactiveRents()).thenReturn(null);

        assertThrows(RentNotFoundException.class, () -> rentService.getInactiveRents());
    }

    @Test
    void shouldGetRentsByItemSuccessfully() {
        when(rentQueryPort.getByItemId("itemId")).thenReturn(Collections.singletonList(rent));

        List<RentDTO> result = rentService.getRentsByItem("itemId");

        assertNotNull(result);
        verify(rentQueryPort).getByItemId("itemId");
    }

    @Test
    void shouldThrowExceptionWhenNoRentsFoundWhileGetRentsByItemId() {
        when(rentQueryPort.getByItemId("itemId")).thenReturn(null);

        assertThrows(RentNotFoundException.class, () -> rentService.getRentsByItem("itemId"));
    }

    @Test
    void shouldGetActiveRentsByItemSuccessfully() {
        when(rentQueryPort.findActiveRentsByItemId("itemId")).thenReturn(Collections.singletonList(rent));

        List<RentDTO> result = rentService.getActiveRentsByItem("itemId");

        assertNotNull(result);
        verify(rentQueryPort).findActiveRentsByItemId("itemId");
    }

    @Test
    void shouldThrowExceptionWhenNoRentsFoundWhileGetActiveRentsByItem() {
        when(rentQueryPort.findActiveRentsByItemId("itemId")).thenReturn(null);

        assertThrows(RentNotFoundException.class, () -> rentService.getActiveRentsByItem("itemId"));
    }

    @Test
    void shouldGetInactiveRentsByItemSuccessfully() {
        when(rentQueryPort.findInactiveRentsByItemId("itemId")).thenReturn(Collections.singletonList(rent));

        List<RentDTO> result = rentService.getInactiveRentsByItem("itemId");

        assertNotNull(result);
        verify(rentQueryPort).findInactiveRentsByItemId("itemId");
    }

    @Test
    void shouldThrowExceptionWhenNoRentsFoundWhileGetInactiveRentsByItem() {
        when(rentQueryPort.findInactiveRentsByItemId("itemId")).thenReturn(null);

        assertThrows(RentNotFoundException.class, () -> rentService.getInactiveRentsByItem("itemId"));
    }

    @Test
    void shouldGetRentsByClientSuccessfully() {
        when(rentQueryPort.getByClientId("clientId")).thenReturn(Collections.singletonList(rent));

        List<RentDTO> result = rentService.getRentsByClient("clientId");

        assertNotNull(result);
        verify(rentQueryPort).getByClientId("clientId");
    }

    @Test
    void shouldThrowExceptionWhenNoRentsFoundWhileGetRentsByClient() {
        when(rentQueryPort.getByClientId("clientId")).thenReturn(null);

        assertThrows(RentNotFoundException.class, () -> rentService.getRentsByClient("clientId"));
    }

    @Test
    void shouldGetActiveRentsByClientSuccessfully() {
        when(rentQueryPort.findActiveRentsByClientId("clientId")).thenReturn(Collections.singletonList(rent));

        List<RentDTO> result = rentService.getActiveRentsByClient("clientId");

        assertNotNull(result);
        verify(rentQueryPort).findActiveRentsByClientId("clientId");
    }

    @Test
    void shouldThrowExceptionWhenNoRentsFoundWhileGetActiveRentsByClient() {
        when(rentQueryPort.findActiveRentsByClientId("clientId")).thenReturn(null);

        assertThrows(RentNotFoundException.class, () -> rentService.getActiveRentsByClient("clientId"));
    }

    @Test
    void shouldGetInactiveRentsByClientSuccessfully() {
        when(rentQueryPort.findInactiveRentsByClientId("clientId")).thenReturn(Collections.singletonList(rent));

        List<RentDTO> result = rentService.getInactiveRentsByClient("clientId");

        assertNotNull(result);
        verify(rentQueryPort).findInactiveRentsByClientId("clientId");
    }

    @Test
    void shouldThrowExceptionWhenNoRentsFoundWhileGetInactiveRentsByClient() {
        when(rentQueryPort.findInactiveRentsByClientId("clientId")).thenReturn(null);

        assertThrows(RentNotFoundException.class, () -> rentService.getInactiveRentsByClient("clientId"));
    }
}
