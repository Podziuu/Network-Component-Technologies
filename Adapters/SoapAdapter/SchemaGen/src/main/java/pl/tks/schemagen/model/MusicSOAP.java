package pl.tks.schemagen.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "Music",
        propOrder = {"genre", "vinyl"}
)
public class MusicSOAP extends ItemSOAP {
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected MusicGenreSOAP genre;
    protected boolean vinyl;

    public MusicGenreSOAP getGenre() {
        return this.genre;
    }

    public void setGenre(MusicGenreSOAP value) {
        this.genre = value;
    }

    public boolean isVinyl() {
        return this.vinyl;
    }

    public void setVinyl(boolean value) {
        this.vinyl = value;
    }
}

