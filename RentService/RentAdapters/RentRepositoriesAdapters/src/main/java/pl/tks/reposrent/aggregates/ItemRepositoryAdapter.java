package pl.tks.reposrent.aggregates;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import pl.tks.modelrent.item.Item;
import pl.tks.portsrent.infrastructure.ItemPort;
import pl.tks.reposrent.mappers.ItemMapper;
import pl.tks.reposrent.repo.ItemRepository;

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
