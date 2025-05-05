package pl.tks.model.client;

public class Client {
    private String id;
    private String firstName;
    private String lastName;
    private int maxArticles;
    private int discount;

    public Client(String id, String firstName, String lastName, int maxArticles, int discount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.maxArticles = maxArticles;
        this.discount = discount;
    }

    public Client(String id, String firstName, String lastName) {
        this(id, firstName, lastName, 2, 0);
    }

    public Client(String firstName, String lastName) {
        this(null, firstName, lastName, 2, 0);
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

    public String getLastName() {
        return lastName;
    }

    public int getMaxArticles() {
        return maxArticles;
    }

    public int getDiscount() {
        return discount;
    }

    public static Client createNoMembershipUser(String id, String firstName, String lastName) {
        return new Client(id, firstName, lastName, 2, 0);
    }
}
