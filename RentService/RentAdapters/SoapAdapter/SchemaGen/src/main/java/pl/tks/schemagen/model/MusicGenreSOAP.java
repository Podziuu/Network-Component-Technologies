package pl.tks.schemagen.model;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(
        name = "MusicGenre"
)
@XmlEnum
public enum MusicGenreSOAP {
    Jazz(1),
    Metal(2),
    Classical(3),
    HipHop(5),
    POP(8);

    private final int value;

    MusicGenreSOAP(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

