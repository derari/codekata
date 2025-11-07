package code.codewars.poker.ranking.patterns;

import code.codewars.poker.ranking.HandTieBreakers;
import code.codewars.poker.ranking.cards.*;

import java.util.Optional;

public class MatchSameRank implements HandPatternMatcher {

    private final GroupSize groupSize;
    private final GroupCount groupCount;

    public MatchSameRank(GroupSize groupSize, GroupCount groupCount) {
        this.groupSize = groupSize;
        this.groupCount = groupCount;
    }

    @Override
    public Optional<HandTieBreakers> match(CardsByRank hand) {
        var groups = hand.getRanksWithCount(groupSize);
        if (!groupCount.match(groups)) return Optional.empty();
        var highCards = groups.getHighCards();
        var kickers = hand.getKickersExcluding(groups::contains);
        return Optional.of(highCards.withKickers(kickers));
    }
}
