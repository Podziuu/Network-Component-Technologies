package pl.tks.reposrent.entities;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class ClientEnt {

    @BsonId
    private ObjectId id;

    @BsonProperty("firstName")
    private String firstName;

    @BsonProperty("lastName")
    private String lastName;

    @BsonProperty("maxArticles")
    private int maxArticles;

    @BsonProperty("discount")
    private int discount;

    @BsonCreator
    public ClientEnt(@BsonProperty("firstName") String firstName,
                     @BsonProperty("lastName") String lastName,
                     @BsonProperty("maxArticles") int maxArticles,
                     @BsonProperty("discount") int discount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.maxArticles = maxArticles;
        this.discount = discount;
    }

    // Konstruktor
    public ClientEnt() {
    }

    // Gettery i Settery
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
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

    public int getMaxArticles() {
        return maxArticles;
    }

    public void setMaxArticles(int maxArticles) {
        this.maxArticles = maxArticles;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "ClientEnt{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", maxArticles=" + maxArticles +
                ", discount=" + discount +
                '}';
    }
}
