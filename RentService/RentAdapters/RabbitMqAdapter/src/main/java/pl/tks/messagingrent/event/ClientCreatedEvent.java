package pl.tks.messagingrent.event;

import java.io.Serializable;

public class ClientCreatedEvent implements Serializable {
    private String id;
    private String firstName;
    private String lastName;

    public int getMaxArticles() {
        return maxArticles;
    }

    public void setMaxArticles(int maxArticles) {
        this.maxArticles = maxArticles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    private int maxArticles;
    private int discount;

    public ClientCreatedEvent() {}

    public ClientCreatedEvent(String id, String firstName, String lastName, int maxArticles, int discount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.maxArticles = maxArticles;
        this.discount = discount;
    }
}
