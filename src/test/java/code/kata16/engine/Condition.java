package code.kata16.engine;

import code.kata16.OtherServices;

public interface Condition<T extends State> {

    boolean test(T state, OtherServices services);

    default Action<T> ifTrue(Action<T> rule) {
        return new Conditional<>(this, rule, new NoOp<>(false));
    }

    default Else<Action<T>, Action<T>> then(Action<T> rule) {
        return orElse -> new Conditional<>(this, rule, orElse);
    }

    default Condition<T> and(Condition<T> other) {
        return new And<>(this, other);
    }

    default Condition<T> is(boolean expected) {
        return expected ? this : not();
    }

    default Condition<T> not() {
        return (state, services) -> !test(state, services);
    }
}
