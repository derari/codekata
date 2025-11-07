package code.codewars.poker;

public enum Suit {

    HEARTS("♥"),
    DIAMONDS("♦"),
    CLUBS("♣"),
    SPADES("♠");

    private final String symbol;

    Suit(String symbol) {
        this.symbol = symbol;
    }

    public String getLetter() {
        return name().substring(0, 1);
    }

    @Override
    public String toString() {
        return symbol;
    }
}
