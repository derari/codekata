package code.kata20;

public enum Suit {

    DIAMONDS("♢"), HEARTS("♡"), SPADES("♠"), CLUBS("♣");

    private String symbol;

    Suit(String symbol) {
        this.symbol = symbol;
    }

    public boolean isRed() {
        return this == DIAMONDS || this == HEARTS;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
