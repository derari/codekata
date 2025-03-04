package code.kata2.peano;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static code.kata2.peano.Peano.zero;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
class PeanoTest {

    @Test
    void inc() {
        assertEquals(0, zero());
        assertEquals(1, zero().inc());
        assertEquals(2, zero().inc().inc());
        assertEquals(3, zero().inc().inc().inc());
    }

    @Test
    void dec() {
        assertEquals(Optional.empty(), zero().dec());
        assertEquals(0, n(1).dec());
        assertEquals(1, n(2).dec());
        assertEquals(2, n(3).dec());
        assertEquals(1, n(3).dec().orElseThrow().dec());
    }

    @Test
    void plus() {
        assertEquals(0, zero().plus(zero()));
        assertEquals(1, zero().plus(n(1)));
        assertEquals(2, zero().plus(n(2)));
        assertEquals(3, zero().plus(n(3)));
        assertEquals(3, n(1).plus(n(2)));
        assertEquals(4, n(2).plus(n(2)));
        assertEquals(5, n(2).plus(n(3)));
        assertEquals(6, n(3).plus(n(3)));
    }

    @Test
    void minus() {
        assertEquals(0, zero().minus(zero()));
        assertEquals(2, n(2).minus(zero()));
        assertEquals(1, n(2).minus(n(1)));
        assertEquals(0, n(2).minus(n(2)));
        assertEquals(Optional.empty(), n(2).minus(n(3)));
    }

    @Test
    void times() {
        assertEquals(0, zero().times(zero()));
        assertEquals(0, zero().times(n(1)));
        assertEquals(0, n(1).times(zero()));
        assertEquals(1, n(1).times(n(1)));
        assertEquals(2, n(1).times(n(2)));
        assertEquals(4, n(2).times(n(2)));
        assertEquals(6, n(2).times(n(3)));
        assertEquals(9, n(3).times(n(3)));
    }

    @Test
    void divMod() {
        assertEquals(dm(0, 1), n(1).divMod(n(2)));
        assertEquals(dm(2, 0), n(2).divMod(n(1)));
        assertEquals(dm(1, 0), n(2).divMod(n(2)));
        assertEquals(dm(0, 2), n(2).divMod(n(3)));
        assertEquals(dm(1, 1), n(3).divMod(n(2)));
        assertEquals(dm(1, 0), n(3).divMod(n(3)));
        assertEquals(dm(1, 1), n(4).divMod(n(3)));
        assertEquals(dm(3, 0), n(6).divMod(n(2)));
    }
    
    private void assertEquals(int expected, Peano actual) {
        assertEquals(expected, actual.asInt());
    }

    private void assertEquals(int expected, Optional<Peano> actual) {
        assertEquals(expected, actual.orElseThrow());
    }

    private void assertEquals(Peano.DivMod expected, Optional<Peano.DivMod> actual) {
        assertEquals(expected, actual.orElseThrow());
    }

    private <T> void assertEquals(T expected, T actual) {
        Assertions.assertEquals(expected, actual);
    }

    private Peano n(int n) {
        var p = zero();
        while (n-- > 0) p = p.inc();
        return p;
    }

    private Peano.DivMod dm(int d, int m) {
        return new Peano.DivMod(n(d), n(m));
    }
}