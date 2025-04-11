package dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ComicsDTO extends ItemDTO {
    @NotNull(message = "Pages number cannot be null")
    @Min(value = 0, message = "Pages number must be at least 0")
    private int pagesNumber;

    @NotNull(message = "Publisher cannot be null")
    private String publisher;

    public ComicsDTO() {

    }

    public ComicsDTO(String id, int basePrice, String itemName, boolean available, int pagesNumber, String publisher) {
        super(id, basePrice, itemName, available);
        this.itemType = "comics";
        this.pagesNumber = pagesNumber;
        this.publisher = publisher;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(int pagesNumber) {
        this.pagesNumber = pagesNumber;
    }

    public @NotNull(message = "Publisher cannot be null") String getPublisher() {
        return publisher;
    }

    public void setPublisher(@NotNull(message = "Publisher cannot be null") String publisher) {
        this.publisher = publisher;
    }
}