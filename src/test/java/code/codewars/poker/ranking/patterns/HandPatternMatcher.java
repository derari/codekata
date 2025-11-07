package code.codewars.poker.ranking.patterns;

import code.codewars.poker.ranking.HandTieBreakers;
import code.codewars.poker.ranking.cards.CardsByRank;

import java.util.*;

public interface HandPatternMatcher {

    Optional<HandTieBreakers> match(CardsByRank hand);
}
