package code.kata.kata20;

public enum Rank {

    ACE, N2, N3, N4, N5, N6, N7, N8, N9, N10, JACK, QUEEN, KING;

    public Rank prev() {
        return values()[ordinal() - 1];
    }

    public Rank next() {
        return values()[ordinal() + 1];
    }

    public boolean topOf(Rank other) {
        return ordinal() - 1 == other.ordinal();
    }

    public boolean ge(Rank other) {
        return ordinal() >= other.ordinal();
    }

    @Override
    public String toString() {
        if (this == N10) return "X";
        var n = name();
        if (n.charAt(0) == 'N') return n.substring(1);
        return n.substring(0, 1);
    }
}
