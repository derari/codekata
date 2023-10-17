package code.kata5;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.util.*;

import static java.lang.System.out;

public class BloomTest {

    final MessageDigest digest;
    final RandomWord randomWord = new RandomWord(5);

    public BloomTest() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("sha-512");
    }

    @Test
    void testSmallList() {
        var words = new HashSet<String>();
        for (int i = 1; i < 10; i++) {
            out.printf("%n== %2d words ==%n%n", i);
            words.add(new String(randomWord.get()));
            testRun(i * 5 * 8, words);
        }
    }

    @Test
    void testWordlist() throws IOException {
        var file = Path.of("src/test/resources/wordlist.txt");
        var dataBits = 8 * Files.size(file);
        var words = new HashSet<>(Files.readAllLines(file));
        testRun(dataBits, words);
    }

    void testRun(long dataBits, Set<String> words) {
        out.println(".1% of false positives");
        out.println("         # of hashes   1   2   3   4   5   6   7   8   9");
        for (var vectorBits = dataBits; vectorBits > dataBits / 33 && vectorBits > 5; vectorBits = vectorBits * 3 / 4) {
            out.printf("%9d bits (%2d%%)", vectorBits, (vectorBits-1) * 100 / dataBits);
            for (var i = 1; i < 10; i++) {
                var bloom = initializeBloomFilter((int) vectorBits, i, words);
                testRun(bloom, words);
            }
            out.println();
        }
    }

    private BloomFilter initializeBloomFilter(int vectorBits, int hashes, Set<String> words) {
        var bloom = new BloomFilter(vectorBits, digest, hashes);
        words.forEach(bloom::add);
        return bloom;
    }

    private void testRun(BloomFilter bloom, Set<String> words) {
        var start = -System.currentTimeMillis();
        var iterations = 0;
        var positives = 0;
        var correct = 0;
        while (start + System.currentTimeMillis() < 300) {
            iterations++;
            var word = randomWord.get();
            if (!bloom.contains(word)) continue;
            positives++;
            if (words.contains(new String(word))) correct++;
        }
        out.printf(" %3d", ((1000 * (positives - correct - 1)) / iterations));
    }


    private static class BloomFilter {
        final BitSet vector = new BitSet();
        final int size;
        final MessageDigest digest;
        final byte[] digestBuf;
        final int[] hashBuf;

        BloomFilter(int size, MessageDigest digest, int hashes) {
            this.size = size;
            this.digest = digest;
            this.digestBuf = digest.digest(new byte[0]);
            this.hashBuf = new int[hashes];
        }

        void add(String word) {
            for (var h: hash(word)) {
                vector.set(h % size);
            }
        }

        boolean contains(byte[] word) {
            for (var h: hash(word)) {
                if (!vector.get(h % size)) return false;
            }
            return true;
        }

        private int[] hash(String word) {
            return hash(word.getBytes());
        }

        private int[] hash(byte[] word) {
            try {
                digest.reset();
                digest.update(word);
                int bytes = digest.digest(digestBuf, 0, digestBuf.length);
                var buf = ByteBuffer.wrap(digestBuf, 0, bytes);
                for (var i = 0; i < hashBuf.length; i++) {
                    hashBuf[i] = buf.getInt() & Integer.MAX_VALUE;
                }
                return hashBuf;
            } catch (DigestException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    }

    private static class RandomWord {
        final byte[] wordBuf;
        final Random random = new Random();

        public RandomWord(int length) {
            this.wordBuf = new byte[length];
        }

        public byte[] get() {
            var value = random.nextLong() & Long.MAX_VALUE;
            for (var i = 0; i < wordBuf.length; i++) {
                wordBuf[i] = (byte) ('a' + value % 26);
                value /= 26;
            }
            return wordBuf;
        }
    }
}
