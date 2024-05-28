package code.kata20;

import java.util.*;

public class KlondikeAI {

    private final KlondikeBoard board;
    private final Set<Card> seen = new HashSet<>();
    private final EnumMap<Suit, Rank> lowestSafe = new EnumMap<>(Map.of(Suit.CLUBS, Rank.ACE, Suit.DIAMONDS, Rank.ACE, Suit.HEARTS, Rank.ACE, Suit.SPADES, Rank.ACE));

    public KlondikeAI(KlondikeBoard board) {
        this.board = board;
    }

    public void play() {
        board.getStock().all().forEach(this::addSeen);
        moveInTableaus();
        scanTableaus();
        board.print();
        finish();
    }

    private void scanTableaus() {
        board.getTableaus().forEach(t -> {
            var n = t.maxDraw();
            if (n <= 1) return;
            t.peek(n - 1).forEach(this::addSeen);
        });
    }

    private void addSeen(Card card) {
        if (seen.add(card)) {
            var suit = card.suit();
            var rank = lowestSafe.get(suit);
            while (rank != Rank.KING) {
                var next = rank.next();
                if (!seen.contains(new Card(next, suit))) {
                    break;
                }
                rank = next;
            }
            lowestSafe.put(suit, rank);
        }
    }

    public boolean moveInTableaus() {
        return moveAnyToAny(board.getTableaus(), board.getTableaus());
    }

    public boolean moveToFoundations() {
        var changed = false;
        for (var t: board.getTableaus()) {
            if (t.isEmpty()) break;
            var top = t.peek(1).get(0);
            if (allowToFoundation(top)) {
                changed |= t.moveToAny(board.getFoundations());
            }
        }
        return changed;
    }

    private boolean allowToFoundation(Card card) {
        if (card.rank() == Rank.ACE) return true;
        var prev = card.rank().prev();
        if (card.suit().isRed()) {
            return lowestSafe.get(Suit.CLUBS).ge(prev)
                    && lowestSafe.get(Suit.SPADES).ge(prev);
        } else {
            return lowestSafe.get(Suit.DIAMONDS).ge(prev)
                    && lowestSafe.get(Suit.HEARTS).ge(prev);
        }
    }

    public boolean finish() {
        var sources = new ArrayList<KlondikeBoard.DrawPile>();
        sources.addAll(board.getTableaus());
        sources.add(board.getStock());
        var targets = new ArrayList<KlondikeBoard.PlacePile>();
        targets.addAll(board.getTableaus());
        targets.addAll(board.getFoundations());
        while (!board.getStock().isEmpty()) {
            var changed = false;
            for (int i = board.getStock().stock().size(); i >= 0; i--) {
                board.getStock().flipNext();
                changed |= moveAnyToAny(sources, targets);
            }
            if (!changed) return false;
            board.print();
        }
        return true;
    }

    public boolean moveAnyToAny(List<? extends KlondikeBoard.DrawPile> sources, List<? extends KlondikeBoard.PlacePile> targets) {
        var changed = false;
        var pending = true;
        while (pending) {
            pending = false;
            for (var t: sources) {
                pending |= t.moveToAny(targets);
            }
            changed |= pending;
        }
        return changed;
    }
}
