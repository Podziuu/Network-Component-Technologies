package pl.tks.service.services;

import pl.tks.ports.infrastructure.ItemPort;
import pl.tks.ports.infrastructure.RentPort;
import pl.tks.model.Rent;
import pl.tks.model.item.Comics;
import pl.tks.model.item.Item;
import pl.tks.model.item.Movie;
import pl.tks.model.item.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tks.ports.ui.IItemPort;
import pl.tks.service.exception.InvalidItemTypeException;
import pl.tks.service.exception.ItemAlreadyRentedException;
import pl.tks.service.exception.ItemNotFoundException;

import java.util.List;

@Service
public class ItemService implements IItemPort {
    private final ItemPort itemPort;
    private final RentPort rentPort;

    @Autowired
    public ItemService(ItemPort itemPort, RentPort rentPort) {
        this.itemPort = itemPort;
        this.rentPort = rentPort;
    }

    @Override
    public Item addItem(Item item) {
        if (!(item instanceof Music || item instanceof Movie || item instanceof Comics)) {
            throw new InvalidItemTypeException("Invalid item type: " + item.getClass().getSimpleName());
        }
        return itemPort.addItem(item);
    }

    @Override
    public Item getItemById(String id) {
        Item item = itemPort.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID: " + id + " not found");
        }
        return item;
    }

    @Override
    public List<Item> getItemsByBasePrice(int basePrice) {
        List<Item> items = itemPort.getItemsByBasePrice(basePrice);
        if (items.isEmpty()) {
            throw new ItemNotFoundException("No items found with basePrice: " + basePrice);
        }
        return items;
    }

    @Override
    public List<Item> getItemsByItemName(String itemName) {
        List<Item> items = itemPort.getItemsByItemName(itemName);
        if (items.isEmpty()) {
            throw new ItemNotFoundException("No items found with itemName: " + itemName);
        }
        return items;
    }

    @Override
    public List<Item> getItemsByItemType(String itemType) {
        List<Item> items = itemPort.getItemsByItemType(itemType);
        if (items.isEmpty()) {
            throw new ItemNotFoundException("No items found with itemType: " + itemType);
        }
        return items;
    }

    @Override
    public void updateItem(String id, Item item) {
        Item existingItem = itemPort.getItemById(id);
        if (existingItem == null) {
            throw new ItemNotFoundException("Item with ID: " + id + " not found");
        }

        existingItem.setBasePrice(item.getBasePrice());
        existingItem.setItemName(item.getItemName());

        if (existingItem instanceof Music music && item instanceof Music updatedMusic) {
            music.setGenre(updatedMusic.getGenre());
            music.setVinyl(updatedMusic.isVinyl());
        } else if (existingItem instanceof Movie movie && item instanceof Movie updatedMovie) {
            movie.setBasePrice(updatedMovie.getBasePrice());
            movie.setItemName(updatedMovie.getItemName());
        } else if (existingItem instanceof Comics comics && item instanceof Comics updatedComics) {
            comics.setPageNumber(updatedComics.getPageNumber());
            comics.setPublisher(updatedComics.getPublisher());
        } else {
            throw new InvalidItemTypeException("Unknown item type: " + item.getClass().getSimpleName());
        }

        itemPort.updateItem(existingItem);
    }

    @Override
    public void removeItem(String id) {
        Item item = itemPort.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID: " + id + " not found");
        }

        List<Rent> activeRents = rentPort.findActiveRentsByItemId(id);
        if (!activeRents.isEmpty()) {
            throw new ItemAlreadyRentedException("Item cannot be removed because it is currently rented.");
        }

        itemPort.removeItem(id);
    }

    @Override
    public void setAvailable(String id) {
        Item item = itemPort.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID: " + id + " not found");
        }
        item.setAvailable(true);
        itemPort.updateItem(item);
    }

    @Override
    public void setUnavailable(String id) {
        Item item = itemPort.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID: " + id + " not found");
        }
        item.setAvailable(false);
        itemPort.updateItem(item);
    }

    @Override
    public List<Item> getAllItems() {
        List<Item> items = itemPort.getAllItems();
        if (items.isEmpty()) {
            throw new ItemNotFoundException("No items found");
        }
        return items;
    }
}
