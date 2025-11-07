package code.codewars.poker;

import code.codewars.poker.ranking.cards.CardsByRank;

import java.util.Collection;
import java.util.List;

public class UnrankedPokerHand {

    private final List<Card> cards;

    public UnrankedPokerHand(Collection<Card> cards) {
        if (cards.isEmpty()) {
            throw new IllegalArgumentException("Empty hand");
        }
        this.cards = cards.stream().sorted().toList();
    }

    public PokerHand ranked() {
        return new PokerHand(this);
    }

    public CardsByRank getCardsByRank() {
        return new CardsByRank(cards);
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
