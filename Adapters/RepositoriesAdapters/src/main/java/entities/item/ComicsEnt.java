package entities.item;

import entities.item.ItemEnt;

public class ComicsEnt extends ItemEnt {
    private int pageNumber;
    private String publisher;

    public ComicsEnt(String id, int basePrice, String itemName, int pageNumber, String publisher) {
        super(id, basePrice, itemName);
        this.itemType = "comics";
        this.pageNumber = pageNumber;
        this.publisher = publisher;
    }

    public ComicsEnt(String id, int basePrice, String itemName, boolean available, int pageNumber, String publisher) {
        super(id, basePrice, itemName, available);
        this.itemType = "comics";
        this.pageNumber = pageNumber;
        this.publisher = publisher;
    }

    public ComicsEnt() {
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