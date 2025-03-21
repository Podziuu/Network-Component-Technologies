package aggregates;

import command.ItemCommandPort;
import entities.item.ItemEnt;
import mappers.ItemMapper;
import model.item.Item;
import query.ItemQueryPort;
import repo.ItemRepository;

import java.util.List;
import java.util.Optional;

public class ItemRepositoryAdapter implements ItemQueryPort, ItemCommandPort {
    private final ItemRepository itemRepository;

    public ItemRepositoryAdapter(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> getAllItems() {
        return List.of();
    }

    @Override
    public Item getItemById(String id) {
        ItemEnt itemEnt = itemRepository.getItemById(id);

        return ItemMapper.toModel(itemEnt);
    }

    @Override
    public List<Item> getItemsByBasePrice(int basePrice) {
        return List.of();
    }

    @Override
    public List<Item> getItemsByItemName(String itemName) {
        return List.of();
    }

    @Override
    public List<Item> getItemsByItemType(String itemType) {
        return List.of();
    }

    @Override
    public Item addItem(Item item) {
        return null;
    }

    @Override
    public Optional<Item> updateItem(Item item) {
        return Optional.empty();
    }

    @Override
    public void removeItem(String id) {

    }
}
