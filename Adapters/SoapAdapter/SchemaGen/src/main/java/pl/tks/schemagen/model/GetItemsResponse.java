package pl.tks.schemagen.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "getItemsResponse", namespace = "http://tks.pl/items")
public class GetItemsResponse {
    List<ItemSOAP> items;

    @XmlElement(name = "items")
    public List<ItemSOAP> getItems() {
        if (items == null) {
            items = new ArrayList<ItemSOAP>();
        }
        return items;
    }

    public void setItems(List<ItemSOAP> items) {
        this.items = items;
    }
}
