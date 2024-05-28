package code.kata20;

import java.util.*;

public record Card(Rank rank, Suit suit) {

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
    public String toString() {
        return suit.toString() + rank;
    }
}
