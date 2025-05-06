package pl.tks.schemagen.model;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "addComicsRequest", namespace = "http://tks.pl/items")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "AddComicsRequest",
        propOrder = {
                "basePrice",
                "itemName",
                "available",
                "itemType",
                "pageNumber",
                "publisher"
        }
)
public class AddComicsRequest {
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "basePrice")
    protected int basePrice;
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "itemName")
    protected String itemName;
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "available")
    protected boolean available;
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "itemType")
    protected String itemType;
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "pageNumber")
    protected int pageNumber;
    @XmlElement(required = true, namespace = "http://tks.pl/items", name = "publisher")
    protected String publisher;
}

