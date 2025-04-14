package pl.tks.ports.infrastructure;

import pl.tks.model.item.Item;

import java.util.List;
public interface ItemPort {
    Item addItem(Item item);
    void updateItem(Item item);
    void removeItem(String id);

    List<Item> getAllItems();

    void deleteAllItems();

    Item getItemById(String id);

    List<Item> getItemsByBasePrice(int basePrice);

    List<Item> getItemsByItemName(String itemName);

    List<Item> getItemsByItemType(String itemType);
}
