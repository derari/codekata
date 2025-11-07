package code.codewars.poker.ranking;

import code.codewars.poker.ranking.cards.*;
import code.codewars.poker.ranking.patterns.*;

import java.util.Optional;

public enum HandPattern {

    HIGH_CARD(new MatchHighCard()),
    PAIR(new MatchSameRank(GroupSize.TWO, GroupCount.SINGLE)),
    TWO_PAIR(new MatchSameRank(GroupSize.TWO, GroupCount.DOUBLE)),
    THREE_OF_A_KIND(new MatchSameRank(GroupSize.THREE, GroupCount.SINGLE)),
    STRAIGHT(new MatchStraight()),
    FLUSH(new MatchFlush()),
    FULL_HOUSE(new MatchFullHouse()),
    FOUR_OF_A_KIND(new MatchSameRank(GroupSize.FOUR, GroupCount.SINGLE)),
//    STRAIGHT_FLUSH,
//    ROYAL_FLUSH,
    ;

    private final HandPatternMatcher matcher;

    HandPattern(HandPatternMatcher matcher) {
        this.matcher = matcher;
    }

    public Optional<HandRank> match(CardsByRank hand) {
        return matcher.match(hand)
                .map(tieBreakers -> new HandRank(this, tieBreakers));
    }
}
