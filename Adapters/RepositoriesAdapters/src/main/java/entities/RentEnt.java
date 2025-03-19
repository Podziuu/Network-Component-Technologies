package entities;

import java.time.LocalDateTime;

import entities.item.ItemEnt;
import entities.user.ClientEnt;

public class RentEnt {
    private String id;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private int rentCost;
    private boolean archive = false;
    private ClientEnt clientEnt;
    private ItemEnt itemEnt;

    public RentEnt(LocalDateTime beginTime, int rentCost, ClientEnt clientEnt, ItemEnt itemEnt) {
        this.beginTime = beginTime;
        this.rentCost = rentCost;
        this.clientEnt = clientEnt;
        this.itemEnt = itemEnt;
    }

    public RentEnt() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        return clientEnt;
    }

    public void setClient(ClientEnt client) {
        this.clientEnt = client;
    }

    public ItemEnt getItem() {
        return itemEnt;
    }

    public void setItem(ItemEnt item) {
        this.itemEnt = item;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public String getRentInfo() {
        return "Rent ID: " + id + ", Client: " + clientEnt.getFirstName() + ", Item: " + itemEnt.getItemName();
    }
}
