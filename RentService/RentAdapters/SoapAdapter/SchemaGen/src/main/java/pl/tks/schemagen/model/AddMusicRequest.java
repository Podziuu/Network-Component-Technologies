package pl.tks.schemagen.model;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "addMusicRequest", namespace = "http://tks.pl/items")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "AddMusicRequest",
        propOrder = {
                "basePrice",
                "itemName",
                "available",
                "itemType",
                "genre",
                "vinyl"
        }
)
public class AddMusicRequest {
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "basePrice")
    protected int basePrice;
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "itemName")
    protected String itemName;
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "available")
    protected boolean available;
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "itemType")
    protected String itemType;
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "genre")
    protected String genre;
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "vinyl")
    protected boolean vinyl;
}

