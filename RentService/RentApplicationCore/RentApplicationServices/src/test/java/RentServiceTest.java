import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tks.model.Rent;
import pl.tks.model.item.Item;
import pl.tks.model.item.Music;
import pl.tks.model.item.MusicGenre;
import pl.tks.model.user.Client;
import pl.tks.model.user.ClientType;
import pl.tks.ports.infrastructure.ItemPort;
import pl.tks.ports.infrastructure.RentPort;
import pl.tks.service.exception.ItemAlreadyRentedException;
import pl.tks.service.exception.ItemNotFoundException;
import pl.tks.service.exception.RentNotFoundException;
import pl.tks.service.exception.UserNotFoundException;
import pl.tks.service.services.RentService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentServiceTest {
    @Mock
    private RentPort rentPort;
    @Mock
    private ItemPort itemPort;

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
        doNothing().when(itemPort).updateItem(any(Item.class));
        when(rentPort.add(any(Rent.class))).thenReturn(rent);

        Rent result = rentService.add(rent);

        assertNotNull(result);
        assertNotNull(result.getBeginTime());
        assertNull(result.getEndTime());
        assertEquals(result.getRentCost(), rent.getRentCost());
        assertFalse(result.getItem().isAvailable());
        verify(rentPort).add(any(Rent.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundWhileRenting() {
        rent.setClient(null);

        assertThrows(UserNotFoundException.class, () -> rentService.add(rent));
    }

    @Test
    void shouldThrowExceptionWhenItemNotFoundWhileRenting() {
        rent.setItem(null);

        assertThrows(ItemNotFoundException.class, () -> rentService.add(rent));
    }

    @Test
    void shouldThrowErrorWhenItemAlreadyRentedWhileRenting() {
        music.setAvailable(false);

        assertThrows(ItemAlreadyRentedException.class, () -> rentService.add(rent));
    }

    @Test
    void shouldGetRentByIdSuccessfully() {
        when(rentPort.getById("rentId")).thenReturn(rent);

        Rent result = rentService.getById("rentId");

        assertNotNull(result);
        assertEquals(result.getRentCost(), rent.getRentCost());
    }

    @Test
    void shouldThrowExceptionWhenRentNotFoundById() {
        when(rentPort.getById("rentId")).thenReturn(null);

        assertThrows(RentNotFoundException.class, () -> rentService.getById("rentId"));
    }

    @Test
    void shouldReturnRentSuccessfully() {
        when(rentPort.getById("rentId")).thenReturn(rent);
        doNothing().when(rentPort).update(any(Rent.class));

        rentService.returnRent("rentId");

        verify(itemPort).updateItem(any(Item.class));
        verify(rentPort).update(any(Rent.class));
    }

//    @Test
//    void shouldThrowExceptionWhenRentNotFoundWhileReturning() {
//        when(rentPort.getById("rentId")).thenReturn(null);
//
//        assertThrows(RentOperationException.class, () -> rentService.returnRent("rentId"));
//    }

    @Test
    void shouldGetActiveRentsSuccessfully() {
        when(rentPort.getActiveRents()).thenReturn(Collections.singletonList(rent));

        List<Rent> result = rentService.getActiveRents();

        assertNotNull(result);
        verify(rentPort).getActiveRents();
    }

    @Test
    void shouldThrowExceptionWhenNoRentsFoundWhileGetActiveRents() {
        when(rentPort.getActiveRents()).thenReturn(null);

        assertThrows(RentNotFoundException.class, () -> rentService.getActiveRents());
    }

    @Test
    void shouldGetInactiveRentsSuccessfully() {
        when(rentPort.getInactiveRents()).thenReturn(Collections.singletonList(rent));

        List<Rent> result = rentService.getInactiveRents();

        assertNotNull(result);
        verify(rentPort).getInactiveRents();
    }

    @Test
    void shouldThrowExceptionWhenNoRentsFoundWhileGetInactiveRents() {
        when(rentPort.getInactiveRents()).thenReturn(null);

        assertThrows(RentNotFoundException.class, () -> rentService.getInactiveRents());
    }

    @Test
    void shouldGetRentsByItemSuccessfully() {
        when(rentPort.getByItemId("itemId")).thenReturn(Collections.singletonList(rent));

        List<Rent> result = rentService.getByItemId("itemId");

        assertNotNull(result);
        verify(rentPort).getByItemId("itemId");
    }

    @Test
    void shouldGetActiveRentsByItemSuccessfully() {
        when(rentPort.findActiveRentsByItemId("itemId")).thenReturn(Collections.singletonList(rent));

        List<Rent> result = rentService.findActiveRentsByItemId("itemId");

        assertNotNull(result);
        verify(rentPort).findActiveRentsByItemId("itemId");
    }

    @Test
    void shouldGetInactiveRentsByItemSuccessfully() {
        when(rentPort.findInactiveRentsByItemId("itemId")).thenReturn(Collections.singletonList(rent));

        List<Rent> result = rentService.findInactiveRentsByItemId("itemId");

        assertNotNull(result);
        verify(rentPort).findInactiveRentsByItemId("itemId");
    }

    @Test
    void shouldGetRentsByClientSuccessfully() {
        when(rentPort.getByClientId("clientId")).thenReturn(Collections.singletonList(rent));

        List<Rent> result = rentService.getByClientId("clientId");

        assertNotNull(result);
        verify(rentPort).getByClientId("clientId");
    }

    @Test
    void shouldGetActiveRentsByClientSuccessfully() {
        when(rentPort.findActiveRentsByClientId("clientId")).thenReturn(Collections.singletonList(rent));

        List<Rent> result = rentService.findActiveRentsByClientId("clientId");

        assertNotNull(result);
        verify(rentPort).findActiveRentsByClientId("clientId");
    }

    @Test
    void shouldGetInactiveRentsByClientSuccessfully() {
        when(rentPort.findInactiveRentsByClientId("clientId")).thenReturn(Collections.singletonList(rent));

        List<Rent> result = rentService.findInactiveRentsByClientId("clientId");

        assertNotNull(result);
        verify(rentPort).findInactiveRentsByClientId("clientId");
    }
}
