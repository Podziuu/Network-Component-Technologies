package query;

import model.item.Item;

import java.util.List;
import java.util.Optional;

public interface ItemQueryPort {
    List<Item> getAllItems();

    Optional<Item> getItemById(String id);
}
