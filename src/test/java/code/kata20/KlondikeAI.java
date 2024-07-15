package code.kata20;

import java.util.*;

public class KlondikeAI {

    private final KlondikeBoard board;

    private final Queue<Snapshot> queue = new PriorityQueue<>();
    private final Set<Long> previous = new HashSet<>();
    private int bestScore = -9999;
    private Snapshot best;

    public KlondikeAI(KlondikeBoard board) {
        this.board = board;
    }

    public KlondikeBoard play0() {
        board.print();
        finish();
        return board;
    }

    public KlondikeBoard play1() {
        board.print();
        move();
        return board;
    }

    /**
     *  -----
     *    1000:    8s 436
     *   10000:   45s 565
     *   50000:  3min 650
     *  100000:  7min 688
     *  500000: 37min 747
     * 1000000: 52min 767
     */
    public KlondikeBoard play2() {
        add(new Snapshot(board));
        while (!queue.isEmpty() && previous.size() < 1000) {
            var s = queue.poll();
            if (s.score > bestScore) {
                bestScore = s.score;
                best = s;
            }
            if (s.board.isWin()) {
//                s.print();
                return s.board;
            }
            move(s);
        }
        best.print();
        return best.board;
    }

    public boolean move() {
        var changed = loopAnyToAny(board.getTableaus(), board.getTableaus());
        if (changed) board.print();

        var pending = true;
        while (pending) {
            pending = false;
            if (!pending) pending |= moveOneToAny(board.getStock().autoflip(), board.getTableaus());
            if (!pending) pending |= moveOneToAny(board.getStock().autoflip(), board.getFoundations());
            if (!pending) pending |= moveOneToAny(board.getTableaus(), board.getFoundations());
            if (pending) {
                if (loopAnyToAny(board.getTableaus(), board.getTableaus())) {
                    board.print();
                }
                changed = true;
            }
        }
        return changed;
    }

    public boolean moveInTableaus() {
        return moveAnyToAny(board.getTableaus(), board.getTableaus());
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
            for (int i = 0; i < board.getStock().stock().size(); i++) {
                board.getStock().flipNext();
                changed |= moveAnyToAny(sources, targets);
            }
            if (!changed) return false;
            board.print();
        }
        return true;
    }

    public boolean loopAnyToAny(Iterable<? extends KlondikeBoard.DrawPile> sources, List<? extends KlondikeBoard.PlacePile> targets) {
        var changed = false;
        while (moveAnyToAny(sources, targets)) { changed = true; }
        return changed;
    }

    public boolean moveAnyToAny(Iterable<? extends KlondikeBoard.DrawPile> sources, List<? extends KlondikeBoard.PlacePile> targets) {
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

    public boolean moveOneToAny(Iterable<? extends KlondikeBoard.DrawPile> sources, List<? extends KlondikeBoard.PlacePile> targets) {
        for (var t: sources) {
            var changed = t.moveToAny(targets);
            if (changed) return true;
        }
        return false;
    }

    private void add(Snapshot s) {
        if (previous.add(s.hash)) queue.add(s);
    }

    private void move(Snapshot s) {
        var board = s.board;
        var board2 = new KlondikeBoard(board);

        for (int i = 0; i < board2.getTableaus().size(); i++) {
            var t = board2.getTableaus().get(i);
            if (t.moveToAny(board2.getTableaus())) {
                add(new Snapshot(board2, s));
                board2 = new KlondikeBoard(board);
                t = board2.getTableaus().get(i);
            }
            if (t.moveToAny(board2.getFoundations())) {
                add(new Snapshot(board2, s));
                board2 = new KlondikeBoard(board);
            }
        }
        for (var c: board.getStock().all()) {
            board2.getStock().find(c);
            for (int i = 0; i < board2.getTableaus().size(); i++) {
                var t = board2.getTableaus().get(i);
                if (t.canPlace(board2.getStock())) {
                    t.place(board2.getStock());
                    add(new Snapshot(board2, s));
                    board2 = new KlondikeBoard(board);
                }
            }
            if (board2.getStock().moveToAny(board2.getFoundations())) {
                add(new Snapshot(board2, s));
                board2 = new KlondikeBoard(board);
            }
        }
        for (int i = 0; i < board2.getFoundations().size(); i++) {
            var f = board2.getFoundations().get(i);
            if (f.moveToAny(board2.getTableaus())) {
                add(new Snapshot(board2, s));
                board2 = new KlondikeBoard(board);
            }
        }
    }

    public record Snapshot(int moves, int score, long hash, KlondikeBoard board, Snapshot previous) implements Comparable<Snapshot> {

        public Snapshot(KlondikeBoard board) {
            this(0, board.score(), board.longhash(), board, null);
        }

        public Snapshot(KlondikeBoard board, Snapshot previous) {
            this(previous.moves + 1, board.score(), board.longhash(), board, previous);
        }

        @Override
        public int compareTo(Snapshot o) {
            int c = o.score - score;
            if (c != 0) return c;
            c = moves - o.moves;
            if (c != 0) return c;
            return (int) (hash - o.hash);
        }

        public void print() {
            if (previous != null) previous.print();
            board.print();
        }
    }
}
