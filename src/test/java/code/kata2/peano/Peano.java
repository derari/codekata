package code.kata2.peano;

import code.kata2.Trampoline;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "java:S125"})
public interface Peano {

    static Peano zero() {
        return Zero.ZERO;
    }

    default Peano inc() {
        return new Successor(this);
    }

//    <T> T apply(T zero, UnaryOperator<T> successor);

    default <T> T apply(T zero, UnaryOperator<T> successor) {
        return bounce(zero, successor).value();
    }

    <T> Trampoline<T> bounce(T zero, UnaryOperator<T> successor);

    abstract class Base implements Peano {

        private Peano successor = null;

        @Override
        public Peano inc() {
            if (successor == null) successor = Peano.super.inc();
            return successor;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Peano p && eq(p);
        }

        @Override
        public int hashCode() {
            return 0x7e3979b9 ^ asInt();
        }

        @Override
        public String toString() {
            return "P{" + asInt() + "}";
        }
    }

    class Zero extends Base {

        private static final Zero ZERO = new Zero();
        private static final Optional<Peano> OPT_ZERO = Optional.of(ZERO);

        private Zero() {
        }

        @Override
        public <T> T apply(T zero, UnaryOperator<T> successor) {
            return zero;
        }

        @Override
        public <T> Trampoline<T> bounce(T zero, UnaryOperator<T> successor) {
            return Trampoline.done(zero);
        }
    }

    class Successor extends Base {

        private final Peano prev;

        private Successor(Peano prev) {
            this.prev = prev;
        }

//        @Override
//        public <T> T apply(T zero, UnaryOperator<T> successor) {
//            return prev.apply(successor.apply(zero), successor);
//        }

        @Override
        public <T> Trampoline<T> bounce(T zero, UnaryOperator<T> successor) {
            var next = successor.apply(zero);
            return () -> prev.bounce(next, successor);
        }

//        @Override
//        public Optional<Peano> dec() {
//            return Optional.of(prev);
//        }
    }

    default int asInt() {
        return apply(0, x -> x + 1);
    }

    default Optional<Peano> dec() {
        return apply(Optional.empty(), p -> p.map(Peano::inc).or(() -> Zero.OPT_ZERO));
    }

    default Peano plus(Peano other) {
        return other.apply(this, Peano::inc);
    }

    default Optional<Peano> minus(Peano other) {
        return other.apply(Optional.of(this), op -> op.flatMap(Peano::dec));
    }

    default Peano times(Peano other) {
        return apply(zero(), other::plus);
    }

    default boolean isZero() {
        return apply(true, t -> false);
    }

    default boolean eq(Peano other) {
        return minus(other).map(Peano::isZero).orElse(false);
    }

    default boolean lt(Peano other) {
        return minus(other).isEmpty();
    }

    default boolean gt(Peano other) {
        return other.lt(this);
    }

    default <T> Optional<T> ifPositive(Supplier<T> supplier) {
        return apply(Optional.empty(), opt -> opt.or(() -> Optional.of(supplier.get())));
    }

    default Optional<DivMod> divMod(Peano other) {
        return other.ifPositive(() -> apply(new DivMod(this), dm -> dm.dec(other)));
    }

    record DivMod(Peano div, Peano mod) {

        DivMod(Peano mod) {
            this(zero(), mod);
        }

        private DivMod dec(Peano other) {
            return mod.minus(other).map(m -> new DivMod(div.inc(), m)).orElse(this);
        }
    }
}
