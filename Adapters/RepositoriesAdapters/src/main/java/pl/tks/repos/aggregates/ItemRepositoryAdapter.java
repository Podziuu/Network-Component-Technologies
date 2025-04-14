package pl.tks.repos.aggregates;

import pl.tks.ports.infrastructure.ItemPort;
import org.springframework.stereotype.Component;
import pl.tks.repos.mappers.ItemMapper;
import pl.tks.model.item.Item;
import org.bson.types.ObjectId;
import pl.tks.repos.repo.ItemRepository;

import java.util.List;
@Component
public class ItemRepositoryAdapter implements ItemPort {
    private final ItemRepository itemRepository;

    public ItemRepositoryAdapter(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> getAllItems() {
        return ItemMapper.toItemList(itemRepository.getAllItems());
    }

    @Override
    public Item getItemById(String id) {
        return ItemMapper.toItem(itemRepository.getItemById(id));
    }

    @Override
    public List<Item> getItemsByBasePrice(int basePrice) {
        return ItemMapper.toItemList(itemRepository.getItemsByBasePrice(basePrice));
    }

    @Override
    public List<Item> getItemsByItemName(String itemName) {
        return ItemMapper.toItemList(itemRepository.getItemsByItemName(itemName));
    }

    @Override
    public List<Item> getItemsByItemType(String itemType) {
        return ItemMapper.toItemList(itemRepository.getItemsByItemType(itemType));
    }

    @Override
    public void deleteAllItems() {
        itemRepository.deleteAllItems();
    }

    @Override
    public Item addItem(Item item) {
        return ItemMapper.toItem(itemRepository.addItem(ItemMapper.toItemEnt(item)));
    }

    @Override
    public void updateItem(Item item) {
        itemRepository.updateItem(ItemMapper.toItemEnt(item));
    }

    @Override
    public void removeItem(String id) {
        itemRepository.removeItem(new ObjectId(id));
    }
}
