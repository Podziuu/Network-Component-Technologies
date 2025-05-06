package pl.tks.schemagen.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "Comics",
        propOrder = {"pageNumber", "publisher"}
)
public class ComicsSOAP extends ItemSOAP {
    protected int pageNumber;
    @XmlElement(
            required = true
    )
    protected String publisher;

    public int getPageNumber() {
        return this.pageNumber;
    }

    public void setPageNumber(int value) {
        this.pageNumber = value;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String value) {
        this.publisher = value;
    }
}