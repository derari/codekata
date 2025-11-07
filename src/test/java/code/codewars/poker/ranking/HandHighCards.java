package code.codewars.poker.ranking;

import code.codewars.poker.Card;

import java.util.List;

public class HandHighCards implements Comparable<HandHighCards> {

    private final Card card1;
    private final Card card2;

    public HandHighCards(Card card1) {
        this(card1, null);
    }

    public HandHighCards(Card card1, Card card2) {
        if (card1 == null && card2 != null) throw new IllegalArgumentException("card1 is null but card2 is not");
        this.card1 = card1;
        this.card2 = card2;
    }

    public HandTieBreakers withKickers(HandKickers kickers) {
        return new HandTieBreakers(this, kickers);
    }

    public HandTieBreakers withoutKickers() {
        return withKickers(HandKickers.empty());
    }

    @Override
    public int compareTo(HandHighCards o) {
        if (card1 == null || o.card1 == null) return 0;
        var c = card1.compareTo(o.card1);
        if (c == 0 && card2 != null && o.card2 != null) c = card2.compareTo(o.card2);
        return c;
    }

    @Override
    public String toString() {
        return "{" + (card1 != null ? card1 + (card2 != null ? "," + card2 : "") : "") + "}";
    }
}
