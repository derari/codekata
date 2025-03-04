package code.commaseq;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CommaSeqTest {

    @Test
    void test1() {
        var n = new Impl(1);
        assertEquals(1, n.next());
        assertEquals(12, n.next());
        assertEquals(35, n.next());
        assertEquals(94, n.next());
        assertEquals(135, n.next());
        assertEquals(186, n.next());
    }

    @Test
    void test3() {
        var n = new Impl(3);
        assertEquals(3, n.next());
        assertEquals(36, n.next());
        assertFalse(n.hasNext());
    }

    @Test
    void max1() {
        max(1);
    }

    @Test
    void max2() {
        for (var i = 2; i < 12; i++) {
            max(i);
        }
    }

    void max(long start) {
        System.out.println("Start: " + start);
        var i = 0;
        var mod = 1;
        var sequence = new Impl(start);
        while (sequence.hasNext() && ++i < 1000000000) {
            var n = sequence.next();
            if (i % mod == 0) {
                if (i == mod * 10) mod *= 10;
                System.out.printf("%10d: %11d%n", i, n);
            }
        }
        System.out.printf("%10d: %11d%n", i, sequence.current);
    }

    private static class Impl implements Iterator<Long> {

        private long current = -1;
        private long next;

        public Impl(long next) {
            this.next = next;
        }

        @Override
        public boolean hasNext() {
            if (next > 0) return true;
            var d = current + 10 * (current % 10);
            for (var i = 1L; i < 10; i++) {
                var n = d + i;
                if (first(n) == i) {
                    next = n;
                    return next > 0;
                }
            }
            return false;
        }

        private long first(long n) {
            while (n >= 1000) n /= 1000;
            while (n >= 10) n /= 10;
            return n;
        }

        @Override
        public Long next() {
            if (!hasNext()) throw new NoSuchElementException("Next of " + current);
            current = next;
            next = -1;
            return current;
        }
    }
}
