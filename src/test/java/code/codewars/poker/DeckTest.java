package code.codewars.poker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeckTest {

    @Test
    void getHand() {
        var deck = new Deck();
        var hand = deck.getHand("9C KD 3D 2H 5S");
        assertEquals("[2♥, 3♦, 5♠, 9♣, K♦]", hand.toString());
    }
}
