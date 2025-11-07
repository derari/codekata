package code.codewars.poker.ranking;

import code.codewars.poker.Deck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HandRankTest {

    @Test
    void highCard() {
        assertBetter("2C 3H 4C 8S AH", "2H 3D 5S 9C KD");
        assertBetter("2C 3H 5C TS KH", "2H 3D 5S 9C KD");
        assertBetter("2C 3H 6C 9S KH", "2H 3D 5S 9C KD");
        assertBetter("2C 4H 5C 9S KH", "2H 3D 5S 9C KD");
        assertBetter("3C 3H 5C 9S KH", "2H 3D 5S 9C KD");
        assertEquals("2C 3H 5C 9S KH", "2H 3D 5S 9C KD");
    }

    @Test
    void onePair_rank() {
        assertBetter("2C 2H 4C 5S 6H", "8D 9H JS QC KD");
    }

    @Test
    void onePair_byValue() {
        assertBetter("2C 5H 7C 6S 6H", "2D 5C 5D 7H JS");
    }

    @Test
    void onePair_byKicker() {
        assertBetter("2C 5H 8C JS JH", "2D 5H 7S JC JD");
        assertBetter("2C 6H 7C JS JH", "2D 5H 7S JC JD");
        assertBetter("3C 5H 7C JS JH", "2D 5H 7S JC JD");
        assertEquals("2C 5H 7C JS JH", "2D 5H 7S JC JD");
    }

    @Test
    void twoPair_rank() {
        assertBetter("2C 2H 8C 8S JH", "9D TH JS QC AD");
        assertBetter("2C 2H 8C 8S JH", "2D 3S 7H KC KD");
    }

    @Test
    void twoPair_byValue() {
        assertBetter("2C 2H 8C 8S JH", "2D 2S 7H 7C JD");
        assertBetter("3C 3H 8C 8S JH", "2D 2S 8H 8C JD");
    }

    @Test
    void twoPair_byKicker() {
        assertBetter("4C 4H 8C 8S JH", "4D 4S 8H 8C TD");
        assertBetter("4C 4H 7H 8C 8S", "4D 4S 6D 8H 8C");
        assertBetter("3H 4C 4H 8C 8S", "2D 4D 4S 8H 8C");
        assertEquals("4C 4H 7H 8C 8S", "4D 4S 7D 8H 8C");
    }

    @Test
    void threeOfAKind_rank() {
        assertBetter("2C 2H 2S 5C 6H", "7D 8H 9S TC KD");
        assertBetter("2C 2H 2S 5C 6H", "7D 8H 9S JC JD");
        assertBetter("2C 2H 2S 5C 6H", "7D 9H 9S JC JD");
    }

    @Test
    void threeOfAKind_byValue() {
        assertBetter("3C 3H 3S 5C 6H", "2D 2H 2S JC QD");
    }

    @Test
    void threeOfAKind_byKicker() {
        assertBetter("3C 3H 3S JC KH", "3D 3S 3H JD QD");
        assertBetter("3C 3H 3S JD KH", "3D 3S 3H TC KD");
        assertBetter("6C 6H 6S 7D KH", "5D 6C 6H 6S KH");
        assertBetter("3D 6C 6H 6S KH", "2D 6C 6H 6S KH");
        assertBetter("2D 6C 6H 6S 7H", "4D 5H 6C 6H 6S");
        assertBetter("2D 5H 6C 6H 6S", "3D 4H 6C 6H 6S");
        assertBetter("3D 5H 6C 6H 6S", "2D 5H 6C 6H 6S");
    }

    @Test
    void straight_rank() {
        assertBetter("2C 3H 4S 5C 6H", "7D 8H 9S TC KD");
        assertBetter("2C 3H 4S 5C 6H", "7D 8H 9S JC JD");
        assertBetter("2C 3H 4S 5C 6H", "7D 9H 9S JC JD");
        assertBetter("2C 3H 4S 5C 6H", "7D 9H JS JC JD");
        assertBetter("2C 3H 4S 5C AH", "7D 9H JS JC JD");
    }

    @Test
    void straight_byValue() {
        assertBetter("3C 4H 5S 6C 7H", "2D 3H 4S 5C 6D");
        assertBetter("TC JH QS KC AH", "9D TH JS QC KD");
        assertBetter("2C 3H 4S 5C 6H", "2D 3H 4S 5C AD");
    }

    @Test
    void flush_rank() {
        assertBetter("2C 3C 4C 5C 7C", "8D TH JS QC KD");
        assertBetter("2C 3C 4C 5C 7C", "9D TH JS KC KD");
        assertBetter("2C 3C 4C 5C 7C", "9D TH KS KC KD");
        assertBetter("2C 3C 4C 5C 7C", "9D QH QS KC KD");
        assertBetter("2C 3C 4C 5C 7C", "9D TH JS QC KD");
    }

    @Test
    void flush_byValue() {
        assertBetter("2C 3C 4C 5C KC", "8D 9D TD JD QD");
        assertBetter("2C 3C 4C QC KC", "8D 9D TD JD KD");
        assertBetter("2C 3C JC QC KC", "8D 9D TD QD KD");
        assertBetter("2C TC JC QC KC", "8D 9D JD QD KD");
        assertBetter("9C TC JC QC KC", "8D TD JD QD KD");
        assertEquals("9C TC JC QC KC", "9D TD JD QD KD");
    }

    @Test
    void fullHouse_rank() {
        assertBetter("2C 2H 2S 3C 3H", "7D 8H 9S TC KD");
        assertBetter("2C 2H 2S 3C 3H", "7D 8H 9S JC JD");
        assertBetter("2C 2H 2S 3C 3H", "7D 9H 9S JC JD");
        assertBetter("2C 2H 2S 3C 3H", "7D 9H JS JC JD");
        assertBetter("2C 2H 2S 3C 3H", "7D 8H 9S TC JD");
        assertBetter("2C 2H 2S 3C 3H", "7D 8D 9D TD QD");
    }

    @Test
    void fullHouse_byValue() {
        assertBetter("3C 3H 3S 5C 5H", "2D 2H 2S JC JD");
        assertBetter("3C 3H 3S 5C 5H", "3D 3S 3H 4C 4D");
        assertBetter("3C 3H 5S 5C 5H", "2D 2H 2S JH JD");
    }

    @Test
    void fourOfAKind_rank() {
        assertBetter("2C 2H 2S 2C 3H", "7D 8H 9S TC JD");
        assertBetter("2C 2H 2S 2C 3H", "7D 8H 9S JC JD");
        assertBetter("2C 2H 2S 2C 3H", "7D 9H 9S JC JD");
        assertBetter("2C 2H 2S 2C 3H", "7D 9H JS JC JD");
        assertBetter("2C 2H 2S 2C 3H", "9D 9H JS JC JD");
    }

    @Test
    void fourOfAKind_byValue() {
        assertBetter("5C 5H 5S 5C 8H", "4D 4H 4S 4C JD");
        assertBetter("2H 5C 5H 5S 5C", "4D 4H 4S 4C JD");
        assertBetter("2H 5C 5H 5S 5C", "3D 4D 4H 4S 4C");
    }

    @Test
    void fourOfAKind_byKicker() {
        assertBetter("5C 5H 5S 5C JH", "5D 5H 5S 5C 8D");
        assertBetter("5C 5H 5S 5C 6H", "4D 5D 5H 5S 5C");
        assertBetter("3H 5C 5H 5S 5C", "2C 5D 5D 5H 5S");
    }

    void assertBetter(String hand1, String hand2) {
        var h1 = DECK.getHand(hand1).ranked();
        var h2 = DECK.getHand(hand2).ranked();
        var c = Math.min(1, h1.compareTo(h2));
        Assertions.assertEquals(1, c,  () -> h1 + " should be better than " + h2);
    }

    void assertEquals(String hand1, String hand2) {
        var h1 = DECK.getHand(hand1).ranked();
        var h2 = DECK.getHand(hand2).ranked();
        var c = h1.compareTo(h2);
        Assertions.assertEquals(0, c,  () -> h1 + " should be equal to " + h2);
    }

    private static final Deck DECK = new Deck();
}
