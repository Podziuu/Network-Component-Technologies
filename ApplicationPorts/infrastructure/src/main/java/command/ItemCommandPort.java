package command;

import model.item.Item;

import java.util.Optional;

public interface ItemCommandPort {
    Item addItem(Item item);
    Optional<Item> updateItem(Item item);
    void removeItem(Item item);
}
