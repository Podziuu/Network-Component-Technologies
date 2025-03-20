package entities.item;

import org.bson.codecs.pojo.annotations.*;
import org.bson.types.ObjectId;

@BsonDiscriminator("movie")
public class MovieEnt extends ItemEnt {

    @BsonProperty("minutes")
    private int minutes;
    @BsonProperty("casette")
    private boolean casette;

    public MovieEnt(
            @BsonProperty("id") ObjectId id,
            @BsonProperty("basePrice") int basePrice,
            @BsonProperty("itemName") String itemName,
            @BsonProperty("minutes") int minutes,
            @BsonProperty("casette") boolean casette) {
        super(id, basePrice, itemName);
        this.itemType = "movie";
        this.minutes = minutes;
        this.casette = casette;
    }

    public MovieEnt(ObjectId id, int basePrice, String itemName, boolean available, int minutes, boolean casette) {
        super(id, basePrice, itemName, available);
        this.itemType = "movie";
        this.minutes = minutes;
        this.casette = casette;
    }

    public MovieEnt() {

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
