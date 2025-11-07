package code.codewars.poker;

public enum Rank {

    N2,
    N3,
    N4,
    N5,
    N6,
    N7,
    N8,
    N9,
    TEN,
    JACK,
    QUEEN,
    KING,
    ACE;

    @Override
    public String toString() {
        var n = name();
        return n.charAt(0) == 'N' ? n.substring(1) : n.substring(0, 1);
    }
}
