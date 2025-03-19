package entities.item;

public enum MusicGenreEnt {
    Jazz(1),
    Metal(2),
    Classical(3),
    HipHop(5),
    POP(8);

    private final int value;

    MusicGenreEnt(int value) {
        this.value = value;
    }


    public int getValue() {
        return value;
    }
}