package code.kata16.engine;

import code.kata16.OtherServices;

import java.util.ArrayList;
import java.util.List;

public record And<T extends State>(List<Condition<T>> conditions) implements Condition<T> {

    public static <T extends State> Condition<T> all(List<? extends Condition<T>> conditions) {
        if (conditions == null) return (a, b) -> false;
        if (conditions.size() == 1) return conditions.get(0);
        return new And<>(new ArrayList<>(conditions));
    }

    @SafeVarargs
    public And(Condition<T>... conditions) {
        this(List.of(conditions));
    }

    @Override
    public boolean test(T state, OtherServices services) {
        return conditions().stream().allMatch(c -> c.test(state, services));
    }

    @Override
    public Condition<T> and(Condition<T> other) {
        var all = new ArrayList<>(conditions());
        if (other instanceof And<T> and) {
            all.addAll(and.conditions);
        } else {
            all.add(other);
        }
        return new And<>(all);
    }
}
