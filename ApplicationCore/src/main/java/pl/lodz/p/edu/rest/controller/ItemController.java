package pl.lodz.p.edu.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.edu.rest.dto.ComicsDTO;
import pl.lodz.p.edu.rest.dto.ItemDTO;
import pl.lodz.p.edu.rest.dto.MovieDTO;
import pl.lodz.p.edu.rest.dto.MusicDTO;
import pl.lodz.p.edu.rest.exception.InvalidItemTypeException;
import pl.lodz.p.edu.rest.service.ItemService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/items")
public class ItemController {
    ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
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
        return itemService.addItem(itemDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO getItem(@PathVariable String id) {
        return itemService.getItemById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/name/{itemName}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> getItemsByItemName(@PathVariable String itemName) {
        return itemService.getItemsByItemName(itemName);
    }

    @GetMapping("/type/{itemType}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> getItemsByItemType(@PathVariable String itemType) {
        return itemService.getItemsByItemType(itemType);
    }

    @GetMapping("/baseprice/{basePrice}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> getItemsByBasePrice(@PathVariable int basePrice) {
        return itemService.getItemsByBasePrice(basePrice);
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
        itemService.updateItem(id, itemDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable String id) {
        itemService.removeItem(id);
    }
}