package pl.tks.restrent.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ItemDTO {
    private String id;
    @NotNull(message = "Base price cannot be null")
    @Min(value = 0, message = "Base price must be at least 0")
    private int basePrice;
    @NotNull(message = "Item name cannot be null")
    private String itemName;
    private boolean available = true;
    @NotNull(message = "Item type cannot be null")
    protected String itemType;

    public ItemDTO() {
    }

    public ItemDTO(String id, int basePrice, String itemName) {
        this.id = id;
        this.basePrice = basePrice;
        this.itemName = itemName;
    }

    public ItemDTO(String id, int basePrice, String itemName, boolean available) {
        this.id = id;
        this.basePrice = basePrice;
        this.itemName = itemName;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
