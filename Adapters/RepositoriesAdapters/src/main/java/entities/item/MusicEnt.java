package entities.item;

public class MusicEnt extends ItemEnt {
    private MusicGenreEnt genre;
    private boolean vinyl;

    public MusicEnt(String id, int basePrice, String itemName, MusicGenreEnt genre, boolean vinyl) {
        super(id, basePrice, itemName);
        this.itemType = "music";
        this.genre = genre;
        this.vinyl = vinyl;
    }

    public MusicEnt(String id, int basePrice, String itemName, boolean available, MusicGenreEnt genre, boolean vinyl) {
        super(id, basePrice, itemName, available);
        this.itemType = "music";
        this.genre = genre;
        this.vinyl = vinyl;
    }

    public MusicEnt() {

    }

    public MusicGenreEnt getGenre() {
        return genre;
    }

    public void setGenre(MusicGenreEnt genre) {
        this.genre = genre;
    }

    public boolean isVinyl() {
        return vinyl;
    }

    public void setVinyl(boolean vinyl) {
        this.vinyl = vinyl;
    }
}