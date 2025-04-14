package pl.tks.schemagen.model;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "addComicsResponse", namespace = "http://tks.pl/items")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "AddComicsResponse",
        propOrder = {"item"}
)
public class AddComicsResponse {
    @XmlElement(required = true, name = "item")
    protected ComicsSOAP item;

    public ComicsSOAP getItem() {
        return this.item;
    }

    public void setItem(ComicsSOAP value) {
        this.item = value;
    }
}
