package code.codewars.poker;

import code.codewars.poker.ranking.HandRank;

public class PokerHand implements Comparable<PokerHand> {

    private final UnrankedPokerHand hand;
    private final HandRank rank;

    public PokerHand(UnrankedPokerHand hand) {
        this.hand = hand;
        this.rank = HandRank.of(hand);
    }

    @Override
    public int compareTo(PokerHand o) {
        return rank.compareTo(o.rank);
    }

    @Override
    public String toString() {
        return "{" + hand + rank + "}";
    }
}
