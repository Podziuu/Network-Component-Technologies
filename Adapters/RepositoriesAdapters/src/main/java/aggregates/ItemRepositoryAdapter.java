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
    private final ItemMapper itemMapper;

    public ItemRepositoryAdapter(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public List<Item> getAllItems() {
        return List.of();
    }

    @Override
    public Optional<Item> getItemById(String id) {
        ItemEnt itemEnt = itemRepository.getItemById(id);

        if (itemEnt == null) {
            return Optional.empty();
        }

        Item item = itemMapper.toModel(itemEnt);
        return Optional.of(item);
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
    public void removeItem(Item item) {

    }
}