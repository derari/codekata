package code.codewars.poker.ranking.patterns;

import code.codewars.poker.ranking.HandHighCards;
import code.codewars.poker.ranking.HandTieBreakers;
import code.codewars.poker.ranking.cards.CardsByRank;

import java.util.Optional;

public class MatchFlush implements HandPatternMatcher {

    @Override
    public Optional<HandTieBreakers> match(CardsByRank hand) {
        return hand.getFlush()
            .map(cards -> {
                var kickers = hand.getKickersExcluding(c -> !cards.contains(c));
                return new HandHighCards(null).withKickers(kickers);
            });
    }
}
