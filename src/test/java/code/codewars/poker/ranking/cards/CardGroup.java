package code.codewars.poker.ranking.cards;

import code.codewars.poker.Card;

import java.util.*;
import java.util.stream.Stream;

public class CardGroup implements Comparable<CardGroup> {

    private final List<Card> cards = new ArrayList<>();

    public CardGroup() {
    }

    public CardGroup(Collection<Card> cards) {
        this.cards.addAll(cards);
    }

    void add(Card card) {
        cards.add(card);
    }

    int size() {
        return cards.size();
    }

    Stream<? extends Card> stream() {
        return cards.stream();
    }

    public Card first() {
        return cards.getFirst();
    }

    @Override
    public int compareTo(CardGroup o) {
        if (cards.isEmpty() || o.cards.isEmpty()) return 0;
        return first().compareTo(o.first());
    }

    public boolean contains(Card card) {
        return cards.contains(card);
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
