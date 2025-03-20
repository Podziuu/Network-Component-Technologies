package dto;

import entities.item.MusicGenreEnt;
import jakarta.validation.constraints.NotNull;

public class MusicEntDTO extends ItemEntDTO {
    @NotNull(message = "Genre cannot be null")
    private MusicGenreEnt genre;
    @NotNull(message = "Vinyl cannot be null")
    private boolean vinyl;

    public MusicEntDTO() {

    }

    public MusicEntDTO(String id, int basePrice, String itemName, boolean available, MusicGenreEnt genre, boolean vinyl) {
        super(id, basePrice, itemName, available);
        this.itemType = "music";
        this.genre = genre;
        this.vinyl = vinyl;
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