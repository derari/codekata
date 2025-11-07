package code.codewars.poker.ranking.patterns;

import code.codewars.poker.ranking.HandHighCards;
import code.codewars.poker.ranking.HandTieBreakers;
import code.codewars.poker.ranking.cards.CardsByRank;

import java.util.Optional;

public class MatchHighCard implements HandPatternMatcher {

    @Override
    public Optional<HandTieBreakers> match(CardsByRank hand) {
        var card = hand.getHighCard();
        var highCards = new HandHighCards(card);
        var kickers = hand.getKickersExcluding(card::equals);
        return Optional.of(highCards.withKickers(kickers));
    }
}
