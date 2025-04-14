package pl.tks.schemagen.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "Item",
        propOrder = {"id", "basePrice", "itemName", "available", "itemType"}
)
@XmlSeeAlso({MovieSOAP.class, ComicsSOAP.class, MusicSOAP.class})
public abstract class ItemSOAP {
    @XmlElement(
            required = true
    )
    protected String id;
    protected int basePrice;
    @XmlElement(
            required = true
    )
    protected String itemName;
    protected boolean available;
    @XmlElement(
            required = true
    )
    protected String itemType;

    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public int getBasePrice() {
        return this.basePrice;
    }

    public void setBasePrice(int value) {
        this.basePrice = value;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String value) {
        this.itemName = value;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void setAvailable(boolean value) {
        this.available = value;
    }

    public String getItemType() {
        return this.itemType;
    }

    public void setItemType(String value) {
        this.itemType = value;
    }
}

