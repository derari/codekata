package code.kata20;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

class KlondikeAITest {

    @Test
    void play_1337() {
        assertTrue(play1(1337));
    }

    @Test
    void play_1338() {
        assertTrue(play1(1338));
    }

    @Test
    void play_1339() {
        assertTrue(play1(1339));
    }

    @Test
    void play_1340() {
        assertTrue(play1(1340));
    }

    @Test
    void play_1341() {
        assertTrue(play1(1341));
    }

    @Test
    void play2_1337() {
        assertTrue(play2(1337));
    }

    @Test
    void play0() {
        play1000(KlondikeAI::play0);
    }

    @Test
    void play1() {
        play1000(KlondikeAI::play1);
    }

    @Test
    void play2() {
        play1000(KlondikeAI::play2);
    }

    boolean play0(long seed) {
        return play(seed, KlondikeAI::play0);
    }

    boolean play1(long seed) {
        return play(seed, KlondikeAI::play1);
    }

    boolean play2(long seed) {
        return play(seed, KlondikeAI::play2);
    }

    void play1000(Function<KlondikeAI, KlondikeBoard> action) {
        int wins = 0;
        for (int i = 10000; i < 11000; i++) {
            if (play(i, action)) wins++;
            System.out.println("=======================");
        }
        System.out.println("Wins: " + wins);
    }

    boolean play(long seed, Function<KlondikeAI, KlondikeBoard> action) {
        var board = new KlondikeBoard(new Random(seed));
        board = action.apply(new KlondikeAI(board));
        board.print(System.out, true);
        return board.isWin();
    }
}