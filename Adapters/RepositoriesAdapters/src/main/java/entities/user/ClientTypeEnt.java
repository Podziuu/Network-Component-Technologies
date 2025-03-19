package entities.user;

public class ClientTypeEnt {
    protected int maxArticles;
    protected int discount;

    public ClientTypeEnt(int maxArticles, int discount) {
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