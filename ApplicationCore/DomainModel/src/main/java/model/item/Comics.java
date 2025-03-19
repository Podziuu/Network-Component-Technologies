package model.item;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@BsonDiscriminator("comics")
public class Comics extends Item {
    @BsonProperty("pageNumber")
    private int pageNumber;

    @BsonProperty("publisher")
    private String publisher;

    public Comics(
                  @BsonProperty("id") ObjectId id,
                  @BsonProperty("basePrice") int basePrice,
                  @BsonProperty("itemName") String itemName,
                  @BsonProperty("pageNumber") int pageNumber,
                  @BsonProperty("publisher") String publisher) {
        super(id, basePrice, itemName);
        this.itemType = "comics";
        this.pageNumber = pageNumber;
        this.publisher = publisher;
    }

    public Comics(ObjectId id, int basePrice, String itemName, boolean available, int pageNumber, String publisher) {
        super(id, basePrice, itemName, available);
        this.itemType = "comics";
        this.pageNumber = pageNumber;
        this.publisher = publisher;
    }

    public Comics() {

    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}