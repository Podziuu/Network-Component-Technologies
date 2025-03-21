package entities.item;

import model.item.MusicGenre;

public enum MusicGenreEnt {
    Jazz(1),
    Metal(2),
    Classical(3),
    HipHop(5),
    POP(8);

    private final int value;
    private String name;

    MusicGenreEnt(int value) {
        this.value = value;
    }


    public int getValue() {
        return value;
    }

    public static MusicGenre toMusicGenre(MusicGenreEnt musicGenreEnt) {
        switch (musicGenreEnt) {
            case Jazz:
                return MusicGenre.Jazz;
            case Metal:
                return MusicGenre.Metal;
            case Classical:
                return MusicGenre.Classical;
            case HipHop:
                return MusicGenre.HipHop;
            case POP:
                return MusicGenre.POP;
            default:
                throw new IllegalArgumentException("Unknown MusicGenreEnt: " + musicGenreEnt);
        }
    }

}