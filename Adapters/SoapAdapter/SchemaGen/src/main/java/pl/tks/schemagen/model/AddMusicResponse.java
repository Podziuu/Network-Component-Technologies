package pl.tks.schemagen.model;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "addMusicResponse", namespace = "http://tks.pl/items")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "AddMusicResponse",
        propOrder = {"item"}
)
public class AddMusicResponse {
    @XmlElement(required = true, name = "item")
    protected MusicSOAP item;

    public MusicSOAP getItem() {
        return this.item;
    }

    public void setItem(MusicSOAP value) {
        this.item = value;
    }
}
