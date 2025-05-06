package pl.tks.schemagen.model;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "addMovieRequest", namespace = "http://tks.pl/items")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "AddMovieRequest",
        propOrder = {
                "basePrice",
                "itemName",
                "available",
                "itemType",
                "minutes",
                "casette"
        }
)
public class AddMovieRequest {
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "basePrice")
    protected int basePrice;
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "itemName")
    protected String itemName;
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "available")
    protected boolean available;
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "itemType")
    protected String itemType;
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "minutes")
    protected int minutes;
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "casette")
    protected boolean casette;
}

