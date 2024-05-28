package code.kata20;

import java.io.*;
import java.util.*;

public class KlondikeBoard {

    private final Stock stock = new Stock();
    private final List<Foundation> foundations = List.of(new Foundation(), new Foundation(), new Foundation(), new Foundation());
    private final List<Tableau> tableaus = List.of(new Tableau(), new Tableau(), new Tableau(), new Tableau(), new Tableau(), new Tableau(), new Tableau());

    public KlondikeBoard() {
        this(new Random());
    }

    public KlondikeBoard(Random rnd) {
        var deck = Card.newDeck();
        Collections.shuffle(deck, rnd);
        for (var i = 0; i < 7; i++) {
            for (var j = 0; j <= i; j++) {
                tableaus.get(i).hidden.add(deck.remove(deck.size() - 1));
            }
        }
        stock.stock.addAll(deck);
    }

    public Stock getStock() {
        return stock;
    }

    public List<Foundation> getFoundations() {
        return foundations;
    }

    public List<Tableau> getTableaus() {
        return tableaus;
    }

    public void print() {
        print(System.out);
    }

    public void print(PrintStream out) {
        print(out, false);
    }

    public void print(PrintStream out, boolean peek) {
        out.println();
        out.print("Stk:");
        stock.peek(0);
        stock.discard.forEach(c -> out.print(" " + c));
        stock.stock.forEach(c -> out.print(peek ? " " + c : " **"));
        for (var i = 0; i < foundations.size(); i++) {
            out.print("\nFn" + i + ":");
            var foundation = foundations.get(i);
            foundation.cards.forEach(c -> out.print(" " + c));
        }

        for (var i = 0; i < tableaus.size(); i++) {
            out.print("\nTb" + i + ":");
            var tableau = tableaus.get(i);
            tableau.peek(0);
            tableau.hidden.forEach(c -> out.print(peek ? " " + c : " **"));
            if (peek && !tableau.hidden.isEmpty()) out.print(" #");
            tableau.cards.forEach(c -> out.print(" " + c));
        }
        out.println();
    }

    public record Stock(List<Card> stock, List<Card> discard) implements DrawPile {
        public Stock() {
            this(new ArrayList<>(), new ArrayList<>());
        }

        @Override
        public boolean isEmpty() {
            return stock.isEmpty() && discard.isEmpty();
        }

        @Override
        public int maxDraw() {
            return isEmpty() ? 0 : 1;
        }

        @Override
        public List<Card> draw(int n) {
            var result = peek(n);
            while (n-- > 0) discard.remove(discard.size() - 1);
            return result;
        }

        @Override
        public List<Card> peek(int n) {
            if (n < 0) throw new IllegalArgumentException("Cannot peek negative cards");
            if (n > maxDraw()) throw new NoSuchElementException("Stock is empty");
            if (discard.isEmpty() && !stock().isEmpty()) {
                flipNext();
            }
            return n == 0 ? List.of() : List.of(discard.get(discard.size() - 1));
        }

        public void flipNext() {
            if (isEmpty()) throw new IllegalStateException("Stock is empty");
            if (stock.isEmpty()) {
                stock.addAll(discard);
                discard.clear();
                Collections.reverse(stock);
            }
            discard.add(stock.remove(stock.size() - 1));
        }

        public boolean find(Suit suit, Rank rank) {
            for (var c: all()) {
                if (c.suit() == suit && c.rank() == rank) {
                    return true;
                }
            }
            return false;
        }

        public Stock seek(Suit suit, Rank rank) {
            if (!find(suit, rank)) {
                throw new IllegalStateException("Card not found: " + suit + rank);
            }
            return this;
        }

        public Iterable<Card> all() {
            return () ->  iterator();
        }

        public Iterator<Card> iterator() {
            if (isEmpty()) return Collections.emptyIterator();
            return new Iterator<>() {
                int remaining = stock.size() + discard.size();
                @Override
                public boolean hasNext() {
                    return remaining > 0;
                }

                @Override
                public Card next() {
                    if (remaining < stock.size() + discard.size()) {
                        flipNext();
                    }
                    remaining--;
                    return peek(1).get(0);
                }
            };
        }
    }

    public record Foundation(List<Card> cards) implements PlacePile {
        public Foundation() {
            this(new ArrayList<>());
        }

        @Override
        public boolean canPlace(DrawPile source) {
            if (source.isEmpty()) return false;
            var next = source.peek(1).get(0);
            if (cards.isEmpty()) return next.rank() == Rank.ACE;
            var top = cards.get(cards.size() - 1);
            return top.suit() == next.suit() && next.rank().topOf(top.rank());
        }

        @Override
        public boolean tryPlace(DrawPile source) {
            if (!canPlace(source)) return false;
            cards.addAll(source.draw(1));
            return true;
        }
    }

    public record Tableau(ArrayList<Card> hidden, List<Card> cards) implements DrawPile, PlacePile {
        public Tableau() {
            this(new ArrayList<>(), new ArrayList<>());
        }

        @Override
        public boolean isEmpty() {
            return hidden.isEmpty() && cards.isEmpty();
        }

        @Override
        public int maxDraw() {
            if (isEmpty()) return 0;
            int size = cards.size();
            if (size == 0) {
                cards.add(hidden.remove(hidden.size() - 1));
                return 1;
            }
//            for (int i = 1; i < size; i++) {
//                if (!topOf(cards.get(size - i - 1), cards.get(size - i))) {
//                    return i;
//                }
//            }
            return size;
        }

        @Override
        public List<Card> draw(int n) {
            var result = peek(n);
            while (n-- > 0) cards.remove(cards.size() - 1);
            return result;
        }

        @Override
        public List<Card> peek(int n) {
            if (n < 0) throw new IllegalArgumentException("Cannot peek negative cards");
            if (n > maxDraw()) throw new NoSuchElementException("Not enough cards");
            return new ArrayList<>(cards.subList(cards.size() - n, cards.size()));
        }

        @Override
        public boolean canPlace(DrawPile source) {
            var n = source.maxDraw();
            if (n == 0) return false;
            var next = source.peek(n);
            if (isEmpty()) return next.get(0).rank() == Rank.KING;
            var top = peek(1).get(0);
            return topOf(top, next.get(0));
        }

        @Override
        public boolean tryPlace(DrawPile source) {
            if (!canPlace(source)) return false;
            cards.addAll(source.draw(source.maxDraw()));
            return true;
        }

        private boolean topOf(Card top, Card next) {
            return top.suit().isRed() != next.suit().isRed()
                    && top.rank().topOf(next.rank());
        }

        public boolean isMovable() {
            if (!hidden.isEmpty()) return true;
            if (cards.isEmpty()) return false;
            return cards.get(0).rank() != Rank.KING;
        }

        public boolean moveToAny(List<? extends PlacePile> pile) {
            var isMovable = !hidden.isEmpty() || cards.isEmpty() || cards.get(0).rank() != Rank.KING;
            for (var p : pile) {
                if ((isMovable || !(p instanceof Tableau)) && p.canPlace(this)) {
                    p.place(this);
                    return true;
                }
            }
            return false;
        }
    }

    public interface DrawPile {

        boolean isEmpty();

        int maxDraw();

        List<Card> draw(int n);

        List<Card> peek(int n);

        default void moveTo(PlacePile pile) {
            pile.place(this);
        }

        default boolean moveToAny(List<? extends PlacePile> pile) {
            for (var p : pile) {
                if (p.canPlace(this)) {
                    p.place(this);
                    return true;
                }
            }
            return false;
        }
    }

    public interface PlacePile {

        boolean canPlace(DrawPile source);

        boolean tryPlace(DrawPile source);

        default void place(DrawPile source) {
            if (!tryPlace(source)) throw new IllegalArgumentException("Cannot place");
        }
    }
}
