package pl.tks.repos.entities;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import pl.tks.repos.entities.item.ItemEnt;
import pl.tks.repos.entities.ClientEnt;

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
    private ClientEnt client;
    @BsonProperty("item")
    private ItemEnt item;

    @BsonCreator
    public RentEnt(@BsonProperty("beginTime") LocalDateTime beginTime,
                   @BsonProperty("rentCost") int rentCost,
                   @BsonProperty("client") ClientEnt client,
                   @BsonProperty("item") ItemEnt item) {
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

    public ClientEnt getClient() {
        return client;
    }

    public void setClient(ClientEnt client) {
        this.client = client;
    }

    public ItemEnt getItem() {
        return item;
    }

    public void setItem(ItemEnt item) {
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
