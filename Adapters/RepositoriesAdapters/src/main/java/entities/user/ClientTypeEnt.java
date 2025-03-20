package entities.user;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class ClientTypeEnt {

    @BsonProperty("max_articles")
    protected int maxArticles;
    @BsonProperty("discount")
    protected int discount;

    @BsonCreator
    public ClientTypeEnt(@BsonProperty("max_articles") int maxArticles,
                         @BsonProperty("discount") int discount) {
        this.maxArticles = maxArticles;
        this.discount = discount;
    }

    public ClientTypeEnt() {

    }

    public int getMaxArticles() {
        return maxArticles;
    }

    public int getDiscount() {
        return discount;
    }

    @BsonIgnore
    public String getClientTypeInfo() {
        return "\nMaksymalna ilość wypożyczonych artykułów: " + this.getMaxArticles();
    }

    public static ClientTypeEnt createDiamondMembership() {
        return new ClientTypeEnt(15, 30);
    }

    public static ClientTypeEnt createMembership() {
        return new ClientTypeEnt(10, 20);
    }

    public static ClientTypeEnt createNoMembership() {
        return new ClientTypeEnt(2, 0);
    }
}