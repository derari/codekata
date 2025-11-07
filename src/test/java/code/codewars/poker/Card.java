package code.codewars.poker;

import java.util.function.BiConsumer;

public class Card implements Comparable<Card> {

    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getKey() {
        return rank + suit.getLetter();
    }

    public void addWithRank(BiConsumer<Rank, Card> addWithRank) {
        addWithRank.accept(rank, this);
    }

    public boolean sameSuit(Suit other) {
        return suit == other;
    }

    @Override
    public int compareTo(Card o) {
        return rank.compareTo(o.rank);
    }

    @Override
    public String toString() {
        return "" + rank + suit;
    }
}
