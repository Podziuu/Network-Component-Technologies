package entities.item;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@BsonDiscriminator("music")
public class MusicEnt extends ItemEnt {

    @BsonProperty("genre")
    private MusicGenreEnt genre;
    @BsonProperty("vinyl")
    private boolean vinyl;

    public MusicEnt(
            @BsonProperty("id") ObjectId id,
            @BsonProperty("basePrice") int basePrice,
            @BsonProperty("itemName") String itemName,
            @BsonProperty("genre") MusicGenreEnt genre,
            @BsonProperty("vinyl") boolean vinyl) {
        super(id, basePrice, itemName);
        this.itemType = "music";
        this.genre = genre;
        this.vinyl = vinyl;
    }

    public MusicEnt(ObjectId id, int basePrice, String itemName, boolean available, MusicGenreEnt genre, boolean vinyl) {
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
