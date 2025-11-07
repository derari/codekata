package code.codewars.poker.ranking.patterns;

import code.codewars.poker.ranking.HandHighCards;
import code.codewars.poker.ranking.HandTieBreakers;
import code.codewars.poker.ranking.cards.CardsByRank;

import java.util.Optional;

public class MatchStraight implements HandPatternMatcher {

    @Override
    public Optional<HandTieBreakers> match(CardsByRank hand) {
        var straight = hand.getStraight();
        if (straight.isEmpty()) return Optional.empty();
        var highCards = new HandHighCards(straight.get().first());
        return Optional.of(highCards.withoutKickers());
    }
}
