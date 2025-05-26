package code.kata.kata16.engine;

import code.kata.kata16.OtherServices;

import java.util.ArrayList;
import java.util.List;

public record Not<T extends State>(Condition<T> condition) implements Condition<T> {

    @Override
    public boolean test(T state, OtherServices services) {
        return !condition.test(state, services);
    }

    @Override
    public Condition<T> not() {
        return condition;
    }
}
