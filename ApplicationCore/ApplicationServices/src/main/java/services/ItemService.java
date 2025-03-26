package services;

import dto.ComicsDTO;
import dto.ItemDTO;
import dto.MovieDTO;
import dto.MusicDTO;
import exception.InvalidItemTypeException;
import exception.ItemAlreadyRentedException;
import exception.ItemNotFoundException;
import infrastructure.ItemCommandPort;
import mapper.ItemMapper;
import model.Rent;
import model.item.Comics;
import model.item.Item;
import model.item.Movie;
import model.item.Music;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import query.ItemQueryPort;
import query.RentQueryPort;

import java.util.List;

@RequestScope
@Service
public class ItemService {
    private final ItemCommandPort itemCommandRepository;
    private final ItemQueryPort itemQueryRepository;
    private final RentQueryPort rentQueryRepository;

    @Autowired
    public ItemService(ItemCommandPort itemCommandRepository, ItemQueryPort itemQueryRepository, RentQueryPort rentQueryRepository) {
        this.itemCommandRepository = itemCommandRepository;
        this.itemQueryRepository = itemQueryRepository;
        this.rentQueryRepository = rentQueryRepository;
    }

    public Item addItem(Item item) {
        if (!(item instanceof Music || item instanceof Movie || item instanceof Comics)) {
            throw new InvalidItemTypeException("Invalid item type: " + item.getClass().getSimpleName());
        }
        return itemCommandRepository.addItem(item);
    }

    public Item getItemById(String id) {
        Item item = itemQueryRepository.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID: " + id + " not found");
        }
        return item;
    }

    public List<Item> getItemsByBasePrice(int basePrice) {
        List<Item> items = itemQueryRepository.getItemsByBasePrice(basePrice);
        if (items == null) {
            throw new ItemNotFoundException("Items with basePrice: " + basePrice + " not found");
        }
        return items;
    }

    public List<Item> getItemsByItemName(String itemName) {
        List<Item> items = itemQueryRepository.getItemsByItemName(itemName);
        if (items == null) {
            throw new ItemNotFoundException("Item with itemName: " + itemName + " not found");
        }
        return items;
    }

    public List<Item> getItemsByItemType(String itemType) {
        List<Item> items = itemQueryRepository.getItemsByItemType(itemType);
        if (items == null) {
            throw new ItemNotFoundException("Item with itemType: " + itemType + " not found");
        }
        return items;
    }

    public void updateItem(String id, Item item) {
        Item existingItem = itemQueryRepository.getItemById(id);
        if (item == null) {
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

        itemCommandRepository.updateItem(item);
    }

    public void removeItem(String id) {
        Item item = itemQueryRepository.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID: " + id + " not found");
        }

        List<Rent> activeRents = rentQueryRepository.findActiveRentsByItemId(id);
        if (!activeRents.isEmpty()) {
            throw new ItemAlreadyRentedException("Item cannot be removed because it is currently rented.");
        }

        itemCommandRepository.removeItem(id);
    }

    public void setAvailable(String id) {
        Item item = itemQueryRepository.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID: " + id + " not found");
        }
        item.setAvailable(true);
        itemCommandRepository.updateItem(item);
    }

    public void setUnavailable(String id) {
        Item item = itemQueryRepository.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID: " + id + " not found");
        }
        item.setAvailable(false);
        itemCommandRepository.updateItem(item);
    }

    public List<Item> getAllItems() {
        List<Item> items = itemQueryRepository.getAllItems();
        if (items == null) {
            throw new ItemNotFoundException("No items found");
        }
        return items;
    }
}
