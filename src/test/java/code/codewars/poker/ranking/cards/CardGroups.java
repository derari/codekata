package code.codewars.poker.ranking.cards;

import code.codewars.poker.Card;
import code.codewars.poker.ranking.HandHighCards;

import java.util.*;

public class CardGroups {

    private final List<CardGroup> groups;

    public CardGroups(List<CardGroup> groups) {
        this.groups = groups.stream()
                .sorted(Comparator.reverseOrder())
                .toList();
    }

    public int size() {
        return groups.size();
    }

    public Card anyCard() {
        return groups.stream()
                .flatMap(CardGroup::stream)
                .findFirst()
                .orElseThrow();
    }

    public HandHighCards getHighCards() {
        if (groups.isEmpty()) throw new IllegalStateException("No groups");
        var card2 = groups.size() > 1 ? groups.get(1).first() : null;
        return new HandHighCards(groups.get(0).first(), card2);
    }

    public boolean contains(Card card) {
        return groups.stream().anyMatch(g -> g.contains(card));
    }

    @Override
    public String toString() {
        return groups.toString();
    }
}
