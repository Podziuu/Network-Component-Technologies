package pl.tks.model.item;

public enum MusicGenre {
    Jazz(1),
    Metal(2),
    Classical(3),
    HipHop(5),
    POP(8);

    private final int value;

    MusicGenre(int value) {
        this.value = value;
    }


    public int getValue() {
        return value;
    }
}
