package query;

import model.item.Item;

import java.util.List;
import java.util.Optional;

public interface ItemQueryPort {
    List<Item> getAllItems();

    Item getItemById(String id);

    List<Item> getItemsByBasePrice(int basePrice);

    List<Item> getItemsByItemName(String itemName);

    List<Item> getItemsByItemType(String itemType);
}
