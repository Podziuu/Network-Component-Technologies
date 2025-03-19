package entities;

import org.bson.types.ObjectId;
import pl.lodz.p.edu.rest.model.item.Item;
import pl.lodz.p.edu.rest.model.user.Client;

import java.time.LocalDateTime;

public class RentEnt {
    @BsonId
    private ObjectId id;
    @BsonProperty("beginTime")
    private LocalDateTime beginTime;
    @BsonProperty("endTime")
    private LocalDateTime endTime;
    @BsonProperty("rentCost")
    private int rentCost;
    @BsonProperty("archive")
    private boolean archive = false;
    @BsonProperty("client")
    private Client client;
    @BsonProperty("item")
    private Item item;

    @BsonCreator
    public RentEnt(@BsonProperty("beginTime") LocalDateTime beginTime,
                   @BsonProperty("rentCost") int rentCost,
                   @BsonProperty("client") Client client,
                   @BsonProperty("item") Item item) {
        this.beginTime = beginTime;
        this.rentCost = rentCost;
        this.client = client;
        this.item = item;
    }

    public RentEnt() {

    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    @BsonIgnore
    public long getRentDays() {
        return java.time.Duration.between(beginTime, endTime).toDays();
    }

    public int getRentCost() {
        return rentCost;
    }

    public void setRentCost(int rentCost) {
        this.rentCost = rentCost;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    @BsonIgnore
    public String getRentInfo() {
        return "Rent ID: " + id + ", Client: " + client.getFirstName() + ", Item: " + item.getItemName();
    }
}
