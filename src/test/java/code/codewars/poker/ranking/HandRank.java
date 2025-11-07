package code.codewars.poker.ranking;

import code.codewars.poker.UnrankedPokerHand;

import java.util.*;

public class HandRank implements Comparable<HandRank> {

    private final HandPattern pattern;
    private final HandTieBreakers tieBreakers;

    public HandRank(HandPattern pattern, HandTieBreakers tieBreakers) {
        this.pattern = pattern;
        this.tieBreakers = tieBreakers;
    }

    @Override
    public int compareTo(HandRank o) {
        var c = pattern.compareTo(o.pattern);
        if (c == 0) c = tieBreakers.compareTo(o.tieBreakers);
        return c;
    }

    @Override
    public String toString() {
        return "{" + pattern + tieBreakers + "}";
    }

    public static HandRank of(UnrankedPokerHand hand) {
        var cardsByRank = hand.getCardsByRank();
        for (var pattern : PATTERNS) {
            var rank = pattern.match(cardsByRank);
            if (rank.isPresent()) return rank.get();
        }
        throw new IllegalStateException("No pattern matched " + hand);
    }

    private static final SortedSet<HandPattern> PATTERNS = new TreeSet<>(Comparator.reverseOrder());

    static {
        PATTERNS.addAll(List.of(HandPattern.values()));
    }
}
