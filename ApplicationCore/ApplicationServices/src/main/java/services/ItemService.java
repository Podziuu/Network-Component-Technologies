package services;

import command.ItemCommandPort;
import dto.ComicsDTO;
import dto.ItemDTO;
import dto.MovieDTO;
import dto.MusicDTO;
import exception.InvalidItemTypeException;
import exception.ItemAlreadyRentedException;
import exception.ItemNotFoundException;
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

    public ItemDTO addItem(ItemDTO itemDTO) {
        ObjectId id;
        Item addedItem;

        switch (itemDTO.getItemType().toLowerCase()) {
            case "music":
                Music music = ItemMapper.toMusic((MusicDTO) itemDTO);
                addedItem = itemCommandRepository.addItem(music);
//                String idAsString1 = id.toHexString();
//                addedItem = itemRepository.getItemById(idAsString1);
                return ItemMapper.toMusicDTO((Music) addedItem);

            case "movie":
                Movie movie = ItemMapper.toMovie((MovieDTO) itemDTO);
                addedItem = itemCommandRepository.addItem(movie);
//                String idAsString2 = id.toHexString();
//                addedItem = itemRepository.getItemById(idAsString2);
                return ItemMapper.toMovieDTO((Movie) addedItem);

            case "comics":
                Comics comics = ItemMapper.toComics((ComicsDTO) itemDTO);
                addedItem = itemCommandRepository.addItem(comics);
//                String idAsString3 = id.toHexString();
//                addedItem = itemRepository.getItemById(idAsString3);
                return ItemMapper.toComicsDTO((Comics) addedItem);

            default:
                throw new InvalidItemTypeException("Invalid item type: " + itemDTO.getItemType());
        }
    }

    public ItemDTO getItemById(String id) {
        Item item = itemQueryRepository.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID: " + id + " not found");
        }
        return ItemMapper.toDTO(item);
    }

    public List<ItemDTO> getItemsByBasePrice(int basePrice) {
        List<Item> items = itemQueryRepository.getItemsByBasePrice(basePrice);
        return ItemMapper.toDTOList(items);
    }

    public List<ItemDTO> getItemsByItemName(String itemName) {
        List<Item> items = itemQueryRepository.getItemsByItemName(itemName);
        return ItemMapper.toDTOList(items);
    }

    public List<ItemDTO> getItemsByItemType(String itemType) {
        List<Item> items = itemQueryRepository.getItemsByItemType(itemType);
        return ItemMapper.toDTOList(items);
    }

    public void updateItem(String id, ItemDTO itemDTO) {
        Item item = itemQueryRepository.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID: " + id + " not found");
        }

        item.setBasePrice(itemDTO.getBasePrice());
        item.setItemName(itemDTO.getItemName());

        switch (itemDTO.getClass().getSimpleName().toLowerCase()) {
            case "musicdto":
                Music music = (Music) item;
                MusicDTO musicDTO = (MusicDTO) itemDTO;
                music.setGenre(musicDTO.getGenre());
                music.setVinyl(musicDTO.isVinyl());
                break;

            case "moviedto":
                Movie movie = (Movie) item;
                MovieDTO movieDTO = (MovieDTO) itemDTO;
                movie.setMinutes(movieDTO.getMinutes());
                movie.setCasette(movieDTO.isCasette());
                break;

            case "comicsdto":
                Comics comics = (Comics) item;
                ComicsDTO comicsDTO = (ComicsDTO) itemDTO;
                comics.setPageNumber(comicsDTO.getPagesNumber());
                comics.setPublisher(comicsDTO.getPublisher());
                break;

            default:
                throw new InvalidItemTypeException("Unknown item type: " + itemDTO.getClass().getSimpleName());
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

//        ObjectId objectId = new ObjectId(id);
        itemCommandRepository.removeItem(id);
    }

    public void setAvailable(ObjectId id) {
        String idAsString = id.toHexString();
        Item item = itemQueryRepository.getItemById(idAsString);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID: " + id + " not found");
        }
        item.setAvailable(true);
        itemCommandRepository.updateItem(item);
    }

    public void setUnavailable(ObjectId id) {
        String idAsString = id.toHexString();
        Item item = itemQueryRepository.getItemById(idAsString);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID: " + id + " not found");
        }
        item.setAvailable(false);
        itemCommandRepository.updateItem(item);
    }

    public List<ItemDTO> getAllItems() {
        List<Item> items = itemQueryRepository.getAllItems();
        return ItemMapper.toDTOList(items);
    }
}
