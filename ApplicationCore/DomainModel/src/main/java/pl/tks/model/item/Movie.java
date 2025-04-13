package pl.tks.model.item;

public class Movie extends Item {
    private int minutes;
    private boolean casette;

    public Movie(String id, int basePrice, String itemName, int minutes, boolean casette) {
        super(id, basePrice, itemName);
        this.itemType = "movie";
        this.minutes = minutes;
        this.casette = casette;
    }

    public Movie(String id, int basePrice, String itemName, boolean available, int minutes, boolean casette) {
        super(id, basePrice, itemName, available);
        this.itemType = "movie";
        this.minutes = minutes;
        this.casette = casette;
    }

    public Movie() {}

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
