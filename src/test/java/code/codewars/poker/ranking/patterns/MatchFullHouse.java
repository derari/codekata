package code.codewars.poker.ranking.patterns;

import code.codewars.poker.ranking.HandHighCards;
import code.codewars.poker.ranking.HandTieBreakers;
import code.codewars.poker.ranking.cards.CardsByRank;
import code.codewars.poker.ranking.cards.GroupSize;

import java.util.Optional;

public class MatchFullHouse implements HandPatternMatcher {

    @Override
    public Optional<HandTieBreakers> match(CardsByRank hand) {
        var trips = hand.getRanksWithCount(GroupSize.THREE);
        if (trips.size() != 1) return Optional.empty();
        var pairs = hand.getRanksWithCount(GroupSize.TWO);
        if (pairs.size() != 1) return Optional.empty();
        var highCards = new HandHighCards(trips.anyCard(), pairs.anyCard());
        return Optional.of(highCards.withoutKickers());
    }
}
