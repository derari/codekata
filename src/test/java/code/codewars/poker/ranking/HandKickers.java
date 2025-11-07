package code.codewars.poker.ranking;

import code.codewars.poker.Card;

import java.util.*;

public class HandKickers implements Comparable<HandKickers> {

    public static HandKickers empty() {
        return new HandKickers(List.of());
    }

    private final List<Card> kickers;

    public HandKickers(Collection<Card> kickers) {
        this.kickers = kickers.stream()
                .sorted(Comparator.reverseOrder())
                .toList();
    }

    @Override
    public int compareTo(HandKickers o) {
        for (var i = 0; i < kickers.size(); i++) {
            var c = kickers.get(i).compareTo(o.kickers.get(i));
            if (c != 0) return c;
        }
        return 0;
    }

    @Override
    public String toString() {
        return kickers.toString();
    }
}
