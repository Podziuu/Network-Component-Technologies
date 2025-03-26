import exception.*;
import infrastructure.ItemCommandPort;
import infrastructure.RentCommandPort;
import model.Rent;
import model.item.Item;
import model.item.Music;
import model.item.MusicGenre;
import model.user.Client;
import model.user.ClientType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import query.RentQueryPort;
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
    private ItemCommandPort itemCommandPort;

    @InjectMocks
    private RentService rentService;

    private Music music;
    private Rent rent;

    @BeforeEach
    void setUp() {
        music = new Music("23", 20, "album", true, MusicGenre.Jazz, false);
        Client client = new Client("123", "JDoe", "password", "Joe", "Doe", ClientType.createNoMembership());
        rent = new Rent("rentId", LocalDateTime.now(), 230, client, music);
    }

    @Test
    void shouldRentItemSuccessfully() {
        doNothing().when(itemCommandPort).updateItem(any(Item.class));
        when(rentCommandPort.add(any(Rent.class))).thenReturn(rent);

        Rent result = rentService.rentItem(rent);

        assertNotNull(result);
        assertNotNull(result.getBeginTime());
        assertNull(result.getEndTime());
        assertEquals(result.getRentCost(), rent.getRentCost());
        assertFalse(result.getItem().isAvailable());
        verify(rentCommandPort).add(any(Rent.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundWhileRenting() {
        rent.setClient(null);

        assertThrows(UserNotFoundException.class, () -> rentService.rentItem(rent));
    }

    @Test
    void shouldThrowExceptionWhenItemNotFoundWhileRenting() {
        rent.setItem(null);

        assertThrows(ItemNotFoundException.class, () -> rentService.rentItem(rent));
    }

    @Test
    void shouldThrowErrorWhenItemAlreadyRenterWhileRenting() {
        music.setAvailable(false);

        assertThrows(ItemAlreadyRentedException.class, () -> rentService.rentItem(rent));
    }

    @Test
    void shouldGetRentByIdSuccessfully() {
        when(rentQueryPort.getById("rentId")).thenReturn(rent);

        Rent result = rentService.getRentById("rentId");

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

        verify(itemCommandPort).updateItem(any(Item.class));
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

        List<Rent> result = rentService.getActiveRents();

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

        List<Rent> result = rentService.getInactiveRents();

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

        List<Rent> result = rentService.getRentsByItem("itemId");

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

        List<Rent> result = rentService.getActiveRentsByItem("itemId");

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

        List<Rent> result = rentService.getInactiveRentsByItem("itemId");

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

        List<Rent> result = rentService.getRentsByClient("clientId");

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

        List<Rent> result = rentService.getActiveRentsByClient("clientId");

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

        List<Rent> result = rentService.getInactiveRentsByClient("clientId");

        assertNotNull(result);
        verify(rentQueryPort).findInactiveRentsByClientId("clientId");
    }

    @Test
    void shouldThrowExceptionWhenNoRentsFoundWhileGetInactiveRentsByClient() {
        when(rentQueryPort.findInactiveRentsByClientId("clientId")).thenReturn(null);

        assertThrows(RentNotFoundException.class, () -> rentService.getInactiveRentsByClient("clientId"));
    }
}
