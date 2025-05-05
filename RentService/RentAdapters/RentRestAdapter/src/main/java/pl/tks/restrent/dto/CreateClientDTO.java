package pl.tks.rest.dto;

import jakarta.validation.constraints.NotNull;

public class CreateClientDTO {

    @NotNull(message = "First name cannot be null")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    private String lastName;

    private int maxArticles = 2;  // Domyślnie 2
    private int discount = 0;      // Domyślnie 0

    private String id;  // Nowe pole ID

    public CreateClientDTO(String firstName, String lastName, int maxArticles, int discount, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.maxArticles = maxArticles;
        this.discount = discount;
    }

    // Gettery i settery dla wszystkich pól
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
