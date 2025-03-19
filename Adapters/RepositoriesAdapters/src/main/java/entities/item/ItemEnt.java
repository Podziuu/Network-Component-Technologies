package entities.item;

public class ItemEnt {
    private String id;
    protected int basePrice;
    protected String itemName;
    protected boolean available;
    protected String itemType;

    public ItemEnt(String id, int basePrice, String itemName) {
        this.id = id;
        this.basePrice = basePrice;
        this.itemName = itemName;
        this.available = true;
    }

    public ItemEnt(String id, int basePrice, String itemName, boolean available) {
        this.id = id;
        this.basePrice = basePrice;
        this.itemName = itemName;
        this.available = available;
    }

    public ItemEnt() {

    }

    public String getItemInfo() {
        return "Item ID: " + id + ", Name: " + itemName + ", Base Price: " + basePrice;
    }

    public int getActualPrice() {
        return basePrice;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}