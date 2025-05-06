package pl.tks.schemagen.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "Movie",
        propOrder = {"minutes", "casette"}
)
public class MovieSOAP extends ItemSOAP {
    protected int minutes;
    protected boolean casette;

    public int getMinutes() {
        return this.minutes;
    }

    public void setMinutes(int value) {
        this.minutes = value;
    }

    public boolean isCasette() {
        return this.casette;
    }

    public void setCasette(boolean value) {
        this.casette = value;
    }
}

