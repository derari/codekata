package code.kata5;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static java.lang.System.out;

public class BloomTest {

    BitSet bloom = new BitSet();
    Set<String> words;
    int size;
    MessageDigest digest;
    int[] hashBuf = new int[2];
    byte[] wordBuf = new byte[5];
    Random random = new Random();

    public BloomTest() throws NoSuchAlgorithmException, IOException {
        digest = MessageDigest.getInstance("sha-512");
        words = new HashSet<>(Files.readAllLines(Path.of("src/test/resources/wordlist.txt")));
    }

    @Test
    void testFoo() throws IOException {
        size = 8 * (int) Files.size(Path.of("src/test/resources/wordlist.txt"));
        while (size > 80000) {
            out.println();
            out.printf("%9d bits", size);
            for (var i = 1; i < 10; i++) {
                hashBuf = new int[i];
                initializeBloomFilter();
                testRun();
            }
            size = (int) (size * 0.75);
        }
        out.println();
//        out.println("=== " + (size/8) + " Bytes ===");
    }

    private void initializeBloomFilter() {
        bloom.clear();
        words.forEach(this::addWord);
    }

    private void addWord(String word) {
        for (var h: hash(word)) {
            bloom.set(h);
        }
    }

    private void testRun() {
        int total = 100000;
        int positives = 0;
        int correct = 0;
        for (int i = 0; i < total; i++) {
            var word = randomWord();
            if (!isInBloomFilter(word)) continue;
            positives++;
            if (words.contains(new String(word))) correct++;
        }
//        out.println("Positives:       " + positives);
//        out.println("Correct Words:   " + correct);
//        out.println("False Pos %:     " + ((100 * (positives - correct)) / total));
        out.printf("  %2d", ((100 * (positives - correct)) / total));
    }

    private byte[] randomWord() {
        for (var i = 0; i < wordBuf.length; i++) {
            wordBuf[i] = (byte) ('a' + random.nextInt(26));
        }
        return wordBuf;
    }

    private boolean isInBloomFilter(byte[] word) {
        for (var h: hash(word)) {
            if (!bloom.get(h)) return false;
        }
        return true;
    }

    private int[] hash(String word) {
        return hash(word.getBytes());
    }

    private int[] hash(byte[] word) {
        digest.reset();
        var hash = digest.digest(word);
        var buf = ByteBuffer.wrap(hash);
        for (var i = 0; i < hashBuf.length; i++) {
            var n = buf.getInt() & Integer.MAX_VALUE;
            hashBuf[i] = n % size;
        }
        return hashBuf;
    }
}
