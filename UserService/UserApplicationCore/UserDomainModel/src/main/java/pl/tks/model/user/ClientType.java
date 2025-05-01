package pl.tks.model.user;

public class ClientType {
    protected int maxArticles;
    protected int discount;

    public ClientType(int maxArticles, int discount) {
        this.maxArticles = maxArticles;
        this.discount = discount;
    }

    public ClientType() {}

    public int getMaxArticles() {
        return maxArticles;
    }

    public int getDiscount() {
        return discount;
    }

    public String getClientTypeInfo() {
        return "\nMaksymalna ilość wypożyczonych artykułów: " + this.getMaxArticles();
    }

    public static ClientType createDiamondMembership() {
        return new ClientType(15, 30);
    }

    public static ClientType createMembership() {
        return new ClientType(10, 20);
    }

    public static ClientType createNoMembership() {
        return new ClientType(2, 0);
    }
}
