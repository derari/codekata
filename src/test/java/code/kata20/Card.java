package code.kata20;

import java.util.*;

public record Card(Rank rank, Suit suit) implements Comparable<Card> {

    public static List<Card> newDeck() {
        var deck = new ArrayList<Card>();
        for (var rank : Rank.values()) {
            for (var suit : Suit.values()) {
                deck.add(new Card(rank, suit));
            }
        }
        return deck;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Card c && rank == c.rank && suit == c.suit;
    }

    @Override
    public int hashCode() {
        return rank.ordinal() * 4 + suit.ordinal();
    }

    @Override
    public String toString() {
        return suit.toString() + rank;
    }

    @Override
    public int compareTo(Card o) {
        return hashCode() - o.hashCode();
    }
}
