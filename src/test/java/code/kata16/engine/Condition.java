package code.kata16.engine;

import code.kata16.OtherServices;

public interface Condition<T extends State> {

    boolean test(T state, OtherServices services);

    default ProcessingRule<T> ifTrue(ProcessingRule<T> rule) {
        return new Conditional<>(this, rule, new NoOp<>(false));
    }

    default Else<ProcessingRule<T>, ProcessingRule<T>> then(ProcessingRule<T> rule) {
        return orElse -> new Conditional<>(this, rule, orElse);
    }

    default Condition<T> and(Condition<T> other) {
        return new And<>(this, other);
    }
}
