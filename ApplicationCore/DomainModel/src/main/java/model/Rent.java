package model;

import model.item.Item;
import model.user.Client;

import java.time.Duration;
import java.time.LocalDateTime;

public class Rent {
    private String id;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private int rentCost;
    private boolean archive = false;
    private Client client;
    private Item item;

    public Rent(LocalDateTime beginTime, int rentCost, Client client, Item item) {
        this.beginTime = beginTime;
        this.rentCost = rentCost;
        this.client = client;
        this.item = item;
    }

    public Rent() {
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
        return (endTime != null) ? Duration.between(beginTime, endTime).toDays() : 0;
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

    public String getRentInfo() {
        return "Rent ID: " + id + ", Client: " + (client != null ? client.getFirstName() : "Unknown") + ", Item: " + (item != null ? item.getItemName() : "Unknown");
    }
}