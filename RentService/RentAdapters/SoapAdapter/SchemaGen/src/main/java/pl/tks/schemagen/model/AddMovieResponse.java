package pl.tks.schemagen.model;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "addMovieResponse", namespace = "http://tks.pl/items")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "AddMovieResponse",
        propOrder = {"item"}
)
public class AddMovieResponse {
    @XmlElement(required = true, name = "item")
    protected MovieSOAP item;

    public MovieSOAP getItem() {
        return this.item;
    }

    public void setItem(MovieSOAP value) {
        this.item = value;
    }
}
