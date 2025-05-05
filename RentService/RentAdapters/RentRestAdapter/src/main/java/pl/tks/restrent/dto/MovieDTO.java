package pl.tks.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MovieDTO extends ItemDTO {
    @NotNull(message = "Minutes cannot be null")
    @Min(value = 0, message = "Minutes must be at least 0")
    private int minutes;
    @NotNull(message = "Casette cannot be null")
    private boolean casette;

    public MovieDTO() {
    }

    public MovieDTO(String id, int basePrice, String itemName, boolean available, int minutes, boolean casette) {
        super(id, basePrice, itemName, available);
        this.itemType = "movie";
        this.minutes = minutes;
        this.casette = casette;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public boolean isCasette() {
        return casette;
    }

    public void setCasette(boolean casette) {
        this.casette = casette;
    }
}
