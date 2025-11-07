package code.codewars.poker.ranking.cards;

import code.codewars.poker.*;
import code.codewars.poker.ranking.HandKickers;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CardsByRank {

    private final Map<Rank, CardGroup> cardsByRank = new TreeMap<>(Comparator.reverseOrder());

    public CardsByRank(Collection<Card> cards) {
        for (var rank: Rank.values()) {
            cardsByRank.put(rank, new CardGroup());
        }
        cards.forEach(c -> c.addWithRank(this::addWithRank));
    }

    private void addWithRank(Rank rank, Card card) {
        cardsByRank.get(rank).add(card);
    }

    public Card getHighCard() {
        return allCards().findFirst().orElseThrow();
    }

    public HandKickers getKickersExcluding(Predicate<Card> exclude) {
        var kickers = allCards()
                .filter(exclude.negate())
                .toList();
        return new HandKickers(kickers);
    }

    private Stream<Card> allCards() {
        return cardsByRank.values().stream().flatMap(CardGroup::stream);
    }

    public CardGroups getRanksWithCount(GroupSize size) {
        var list = cardsByRank.values().stream()
                .filter(g -> size.match(g.size()))
                .toList();
        return new CardGroups(list);
    }

    public Optional<CardGroup> getStraight() {
        var straight = new ArrayList<Card>();
        for (var group: cardsByRank.values()) {
            if (group.size() == 0) {
                straight.clear();
                continue;
            }
            straight.add(group.first());
            if (straight.size() == 5) {
                return Optional.of(new CardGroup(straight));
            }
        }
        if (straight.size() == 4) {
            var aces = cardsByRank.get(Rank.ACE);
            if (aces.size() > 0) {
                straight.add(aces.first());
                return Optional.of(new CardGroup(straight));
            }
        }
        return Optional.empty();
    }

    public Optional<CardGroup> getFlush() {
        for (var suit: Suit.values()) {
            var cards = allCards().filter(card -> card.sameSuit(suit)).toList();
            if (cards.size() >= 5) {
                return Optional.of(new CardGroup(cards.subList(0, 5)));
            }
        }
        return Optional.empty();
    }
}
