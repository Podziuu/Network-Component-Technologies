package infrastructure;

import model.item.Item;

public interface ItemCommandPort {
    Item addItem(Item item);
    void updateItem(Item item);
    void removeItem(String id);
}
