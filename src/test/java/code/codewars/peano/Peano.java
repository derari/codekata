package code.codewars.peano;

import code.kata.kata2.Trampoline;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "java:S125"})
public interface Peano {

    static Peano zero() {
        return Zero.INSTANCE;
    }

    default Peano inc() {
        return new Successor(this);
    }

//    <T> T apply(T zero, UnaryOperator<T> increment);

    default <T> T apply(T zero, UnaryOperator<T> increment) {
        return trampoline(zero, increment).value();
    }

    <T> Trampoline<T> trampoline(T zero, UnaryOperator<T> increment);

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

        private static final Zero INSTANCE = new Zero();
        private static final Optional<Peano> OPT = Optional.of(INSTANCE);
        private static final Supplier<Optional<Peano>> GET_OPT = () -> OPT;

        private Zero() {
        }

//        @Override
//        public <T> T apply(T zero, UnaryOperator<T> increment) {
//            return zero;
//        }

        @Override
        public <T> Trampoline<T> trampoline(T zero, UnaryOperator<T> increment) {
            return Trampoline.done(zero);
        }
    }

    class Successor extends Base {

        private final Peano previous;

        private Successor(Peano previous) {
            this.previous = previous;
        }

//        @Override
//        public <T> T apply(T zero, UnaryOperator<T> increment) {
//            return previous.apply(increment.apply(zero), increment);
//        }

        @Override
        public <T> Trampoline<T> trampoline(T zero, UnaryOperator<T> increment) {
            return () -> previous.trampoline(increment.apply(zero), increment);
        }

//        @Override
//        public Optional<Peano> dec() {
//            return Optional.of(previous);
//        }
    }

    default int asInt() {
        return apply(0, x -> ++x);
    }

    default Optional<Peano> dec() {
        return apply(Optional.empty(), p -> p.map(Peano::inc).or(Zero.GET_OPT));
    }

    default Peano plus(Peano other) {
        return apply(other, Peano::inc);
    }

    default Optional<Peano> minus(Peano other) {
        return other.apply(Optional.of(this), op -> op.flatMap(Peano::dec));
    }

    default Peano times(Peano other) {
        return apply(zero(), other::plus);
    }

    default boolean isZero() {
        return apply(true, b -> false);
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

    default boolean isEven() {
        return apply(true, b -> !b);
    }

    default boolean isOdd() {
        return !isEven();
    }

    default <T> Optional<T> ifPositive(Supplier<T> supplier) {
        return apply(Optional.empty(), opt -> opt.or(() -> Optional.of(supplier.get())));
    }

    default Optional<DivMod> divMod(Peano other) {
        return other.ifPositive(() -> apply(new DivMod(this), dm -> dm.dec(other)));
    }

    record DivMod(Peano div, Peano mod) {

        private DivMod(Peano mod) {
            this(zero(), mod);
        }

        private DivMod dec(Peano other) {
            return mod.minus(other).map(m -> new DivMod(div.inc(), m)).orElse(this);
        }
    }
}
