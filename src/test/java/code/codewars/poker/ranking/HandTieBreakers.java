package code.codewars.poker.ranking;

public class HandTieBreakers implements Comparable<HandTieBreakers> {

    private final HandHighCards highCards;
    private final HandKickers kickers;

    public HandTieBreakers(HandHighCards highCards, HandKickers kickers) {
        this.highCards = highCards;
        this.kickers = kickers;
    }

    @Override
    public int compareTo(HandTieBreakers o) {
        var c = highCards.compareTo(o.highCards);
        if (c == 0) c = kickers.compareTo(o.kickers);
        return c;
    }

    @Override
    public String toString() {
        return "{" + highCards + "," + kickers + "}";
    }
}
