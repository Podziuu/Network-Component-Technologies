import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tks.service.services.ItemService;
import pl.tks.soap.endpoint.ItemEndpoint;
import pl.tks.soap.mapper.ItemMapper;
import pl.tks.items.ObjectFactory;
import generated.*;
import java.util.List;
import javax.xml.namespace.QName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemEndpointTest {
    @Mock
    private ItemMapper itemMapper;

    @Mock
    private ItemService itemService;

    @Mock
    private ItemEndpoint itemEndpoint;

    private final ObjectFactory factory = new ObjectFactory();

    @BeforeEach
    void setup() {
        itemService = mock(ItemService.class);
        itemEndpoint = new ItemEndpoint(itemService);
    }

    @Test
    void getItems_returnsAllItems() {
        // Arrange
        Item item1 = new Comics("Spider-man", BigDecimal.valueOf(10.99), true, "COMICS", 100, "Marvel");
        Item item2 = new Music("Best of 80s", BigDecimal.valueOf(15.00), false, "MUSIC", "Rock", true);

        when(itemService.getAllItems()).thenReturn(List.of(item1, item2));

        GetItemsRequest request = new GetItemsRequest();
        JAXBElement<GetItemsRequest> jaxbRequest = new JAXBElement<>(
                new QName("http://tks.pl/items", "getItemsRequest"),
                GetItemsRequest.class, request);

        // Act
        JAXBElement<GetItemsResponse> response = itemEndpoint.getItems(jaxbRequest);

        // Assert
        assertEquals(2, response.getValue().getItems().size());
        verify(itemService, times(1)).getAllItems();
    }
}
