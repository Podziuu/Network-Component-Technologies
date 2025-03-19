package entities.item;

import org.bson.types.ObjectId;

@BsonDiscriminator("item")
public class ItemEnt {
    @BsonId
    private ObjectId id;
    @BsonProperty("basePrice")
    protected int basePrice;
    @BsonProperty("itemName")
    protected String itemName;
    @BsonProperty("available")
    protected boolean available;
    @BsonProperty("itemType")
    protected String itemType;

    @BsonCreator
    public ItemEnt(
            @BsonProperty("id") ObjectId id,
            @BsonProperty("basePrice") int basePrice,
            @BsonProperty("itemName") String itemName) {
        this.id = id;
        this.basePrice = basePrice;
        this.itemName = itemName;
        this.available = true;
    }

    public ItemEnt(ObjectId id, int basePrice, String itemName, boolean available) {
        this.id = id;
        this.basePrice = basePrice;
        this.itemName = itemName;
        this.available = available;
    }

    public ItemEnt() {

    }

    @BsonIgnore
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

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
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