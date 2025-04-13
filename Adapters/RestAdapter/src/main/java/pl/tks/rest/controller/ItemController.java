package pl.tks.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.tks.model.item.Item;
import pl.tks.ports.ui.IItemPort;
import pl.tks.rest.dto.ComicsDTO;
import pl.tks.rest.dto.ItemDTO;
import pl.tks.rest.dto.MovieDTO;
import pl.tks.rest.dto.MusicDTO;
import pl.tks.rest.mapper.ItemMapper;
import pl.tks.service.exception.InvalidItemTypeException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/items")
public class ItemController {

    private final IItemPort itemPort;

    public ItemController(IItemPort itemPort) {
        this.itemPort = itemPort;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDTO addItem(@RequestBody @Valid Map<String, Object> payload) {
        String itemType = (String) payload.get("itemType");
        ObjectMapper mapper = new ObjectMapper();
        ItemDTO itemDTO = switch (itemType.toLowerCase()) {
            case "music" -> mapper.convertValue(payload, MusicDTO.class);
            case "movie" -> mapper.convertValue(payload, MovieDTO.class);
            case "comics" -> mapper.convertValue(payload, ComicsDTO.class);
            default -> throw new IllegalArgumentException("Invalid item type: " + itemType);
        };
        Item item = itemPort.addItem(ItemMapper.toItem(itemDTO));
        return ItemMapper.toDTO(item);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO getItem(@PathVariable String id) {
        Item item = itemPort.getItemById(id);
        return ItemMapper.toDTO(item);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> getAllItems() {
        List<Item> items = itemPort.getAllItems();
        return ItemMapper.toDTOList(items);
    }

    @GetMapping("/name/{itemName}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> getItemsByItemName(@PathVariable String itemName) {
        List<Item> items = itemPort.getItemsByItemName(itemName);
        return ItemMapper.toDTOList(items);
    }

    @GetMapping("/type/{itemType}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> getItemsByItemType(@PathVariable String itemType) {
        List<Item> items = itemPort.getItemsByItemType(itemType);
        return ItemMapper.toDTOList(items);
    }

    @GetMapping("/baseprice/{basePrice}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> getItemsByBasePrice(@PathVariable int basePrice) {
        List<Item> items = itemPort.getItemsByBasePrice(basePrice);
        return ItemMapper.toDTOList(items);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateItem(@PathVariable String id, @RequestBody Map<String, Object> payload) {
        String itemType = (String) payload.get("itemType");
        ObjectMapper mapper = new ObjectMapper();
        ItemDTO itemDTO = switch (itemType.toLowerCase()) {
            case "music" -> mapper.convertValue(payload, MusicDTO.class);
            case "movie" -> mapper.convertValue(payload, MovieDTO.class);
            case "comics" -> mapper.convertValue(payload, ComicsDTO.class);
            default -> throw new InvalidItemTypeException("Invalid item type: " + itemType);
        };

        Item item = ItemMapper.toItem(itemDTO);
        itemPort.updateItem(id, item);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable String id) {
        itemPort.removeItem(id);
    }
}
