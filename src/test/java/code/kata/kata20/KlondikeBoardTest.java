package code.kata.kata20;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class KlondikeBoardTest {

    @Test
    void stock_draw() {
        KlondikeBoard.Stock stock = new KlondikeBoard.Stock();
        stock.stock().add(new Card(Rank.ACE, Suit.CLUBS));
        stock.stock().add(new Card(Rank.N2, Suit.DIAMONDS));
        stock.stock().add(new Card(Rank.N3, Suit.HEARTS));

        assertEquals(Suit.HEARTS, stock.peek(1).get(0).suit());
        stock.flipNext();
        assertEquals(Suit.DIAMONDS, stock.draw(1).get(0).suit());
        assertEquals(Suit.HEARTS, stock.peek(1).get(0).suit());
        stock.flipNext();
        assertEquals(Suit.CLUBS, stock.peek(1).get(0).suit());
        stock.flipNext();
        assertEquals(Suit.HEARTS, stock.draw(1).get(0).suit());
        assertEquals(Suit.CLUBS, stock.draw(1).get(0).suit());
        assertTrue(stock.isEmpty());
    }

    @Test
    void foundation_place() {
        KlondikeBoard.Stock stock = new KlondikeBoard.Stock();
        stock.discard().add(new Card(Rank.N3, Suit.CLUBS));
        stock.discard().add(new Card(Rank.N2, Suit.CLUBS));
        stock.discard().add(new Card(Rank.ACE, Suit.CLUBS));

        KlondikeBoard.Foundation foundation = new KlondikeBoard.Foundation();
        foundation.place(stock);
        foundation.place(stock);
        foundation.place(stock);

        assertTrue(stock.isEmpty());

        stock.discard().add(new Card(Rank.N9, Suit.DIAMONDS));
        assertFalse(foundation.tryPlace(stock));

        stock.discard().clear();
        stock.discard().add(new Card(Rank.N10, Suit.CLUBS));
        assertFalse(foundation.tryPlace(stock));
    }

    @Test
    void tableau_place() {
        KlondikeBoard.Stock stock = new KlondikeBoard.Stock();
        stock.discard().add(new Card(Rank.JACK, Suit.CLUBS));
        stock.discard().add(new Card(Rank.QUEEN, Suit.HEARTS));
        stock.discard().add(new Card(Rank.KING, Suit.CLUBS));

        KlondikeBoard.Tableau tableau = new KlondikeBoard.Tableau();
        tableau.place(stock);
        tableau.place(stock);
        tableau.place(stock);

        assertTrue(stock.isEmpty());

        stock.discard().add(new Card(Rank.N9, Suit.DIAMONDS));
        assertFalse(tableau.tryPlace(stock));

        stock.discard().clear();
        stock.discard().add(new Card(Rank.N10, Suit.CLUBS));
        assertFalse(tableau.tryPlace(stock));
    }

    @Test
    void game1() {
        var board = new KlondikeBoard(new Random(1337));
        board.print(System.out);

        board.getTableaus().get(4).moveToAny(board.getFoundations());
        board.getTableaus().get(6).moveToAny(board.getFoundations());
        board.getStock().seek(Suit.SPADES, Rank.ACE).moveToAny(board.getFoundations());
        board.getStock().seek(Suit.HEARTS, Rank.N2).moveToAny(board.getFoundations());
        board.getStock().seek(Suit.CLUBS, Rank.N2).moveToAny(board.getFoundations());
        board.getStock().seek(Suit.CLUBS, Rank.N3).moveToAny(board.getFoundations());
        board.print(System.out);

        moveInTableaus(board);
        board.print(System.out);

        board.getStock().seek(Suit.SPADES, Rank.N5).moveToAny(board.getTableaus());
        moveInTableaus(board);
        board.print(System.out);

        board.getStock().seek(Suit.HEARTS, Rank.JACK).moveToAny(board.getTableaus());
        board.getStock().seek(Suit.CLUBS, Rank.N10).moveToAny(board.getTableaus());
        board.getStock().seek(Suit.HEARTS, Rank.N9).moveToAny(board.getTableaus());
        board.getTableaus().get(5).moveTo(board.getTableaus().get(0));
        moveInTableaus(board);
        board.print(System.out);

        board.getStock().seek(Suit.DIAMONDS, Rank.N9).moveToAny(board.getTableaus());
        moveInTableaus(board);
        board.print(System.out);

        moveToFoundations(board);
        moveInTableaus(board);
        board.print(System.out);

        finish(board);
        board.print(System.out);
    }

    public boolean moveInTableaus(KlondikeBoard board) {
        return moveAnyToAny(board.getTableaus(), board.getTableaus());
    }

    public boolean moveToFoundations(KlondikeBoard board) {
        return moveAnyToAny(board.getTableaus(), board.getFoundations());
    }

    public void finish(KlondikeBoard board) {
        var sources = new ArrayList<KlondikeBoard.DrawPile>();
        sources.add(board.getStock());
        sources.addAll(board.getTableaus());
        var targets = new ArrayList<KlondikeBoard.PlacePile>();
        targets.addAll(board.getFoundations());
        targets.addAll(board.getTableaus());
        while (!board.getStock().isEmpty()) {
            board.getStock().flipNext();
            moveAnyToAny(sources, targets);
        }
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
