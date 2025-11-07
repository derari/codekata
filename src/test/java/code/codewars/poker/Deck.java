package code.codewars.poker;

import java.util.*;

public class Deck {

    private final Map<String, Card> cards = new HashMap<>();

    public Deck() {
        for (var suit : Suit.values()) {
            for (var rank : Rank.values()) {
                var card = new Card(rank, suit);
                cards.put(card.getKey(), card);
            }
        }
    }

    public UnrankedPokerHand getHand(String hand) {
        var strings = hand.split("\\s+");
        var result = new ArrayList<Card>(strings.length);
        for (var card: strings) {
            var c = cards.get(card);
            if (c == null) {
                throw new IllegalArgumentException("Unknown card: " + card);
            }
            result.add(c);
        }
        return new UnrankedPokerHand(result);
    }
}
