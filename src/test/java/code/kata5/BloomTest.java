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

    BloomFilter bloom;
    Set<String> words;
    MessageDigest digest;
    byte[] wordBuf = new byte[5];
    Random random = new Random();

    public BloomTest() throws NoSuchAlgorithmException, IOException {
        digest = MessageDigest.getInstance("sha-512");
        words = new HashSet<>(Files.readAllLines(Path.of("src/test/resources/wordlist.txt")));
    }

    @Test
    void testFoo() throws IOException {
        var fileSize = 8 * Files.size(Path.of("src/test/resources/wordlist.txt"));
        out.println(".1% of false positives");
        out.println("         # of hashes   1   2   3   4   5   6   7   8   9");
        for (var bits = fileSize; bits > fileSize / 100; bits = bits * 3 / 4) {
            out.printf("%9d bits (%2d%%)", bits, (bits-1) * 100 / fileSize);
            for (var i = 1; i < 10; i++) {
                initializeBloomFilter((int) bits, i);
                testRun();
            }
            out.println();
        }
    }

    private void initializeBloomFilter(int size, int hashes) {
        bloom = new BloomFilter(size, digest, hashes);
        words.forEach(bloom::add);
    }

    private void testRun() {
        int total = 100000;
        int positives = 0;
        int correct = 0;
        for (int i = 0; i < total; i++) {
            var word = randomWord();
            if (!bloom.contains(word)) continue;
            positives++;
            if (words.contains(new String(word))) correct++;
        }
        out.printf(" %3d", ((1000 * (positives - correct)) / total));
    }

    private byte[] randomWord() {
        var value = random.nextLong() & Long.MAX_VALUE;
        for (var i = 0; i < wordBuf.length; i++) {
            wordBuf[i] = (byte) ('a' + value % 26);
            value /= 26;
        }
        return wordBuf;
    }

    private static class BloomFilter {
        final BitSet vector = new BitSet();
        final int size;
        final MessageDigest digest;
        final int[] hashBuf;

        BloomFilter(int size, MessageDigest digest, int hashes) {
            this.size = size;
            this.digest = digest;
            this.hashBuf = new int[hashes];
        }

        void add(String word) {
            for (var h: hash(word)) {
                vector.set(h);
            }
        }

        boolean contains(byte[] word) {
            for (var h: hash(word)) {
                if (!vector.get(h)) return false;
            }
            return true;
        }

        private int[] hash(String word) {
            return hash(word.getBytes());
        }

        private int[] hash(byte[] word) {
            digest.reset();
            var buf = ByteBuffer.wrap(digest.digest(word));
            for (var i = 0; i < hashBuf.length; i++) {
                var n = buf.getInt() & Integer.MAX_VALUE;
                hashBuf[i] = n % size;
            }
            return hashBuf;
        }
    }
}
