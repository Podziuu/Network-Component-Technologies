package pl.tks.ports.ui;

import pl.tks.model.item.Item;

import java.util.List;

public interface IItemPort {
    Item addItem(Item item);
    void updateItem(String id, Item item);
    void removeItem(String id);

    void setAvailable(String id);

    void setUnavailable(String id);

    List<Item> getAllItems();

    Item getItemById(String id);

    List<Item> getItemsByBasePrice(int basePrice);

    List<Item> getItemsByItemName(String itemName);

    List<Item> getItemsByItemType(String itemType);
}
