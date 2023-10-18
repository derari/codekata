package code.kata16.engine;

import code.kata16.OtherServices;

import java.util.List;

public record Sequence<T extends State>(List<Action<T>> steps) implements Action<T> {

    public static <T extends State> Action<T> of(List<Action<T>> steps) {
        if (steps == null) return new NoOp<>(false);
        if (steps.isEmpty()) return new NoOp<>(true);
        if (steps.size() == 1) return steps.get(0);
        return new Sequence<>(steps);
    }

    @SafeVarargs
    public Sequence(Action<T>... steps) {
        this(List.of(steps));
    }

    @Override
    public boolean apply(T state, OtherServices services) {
        state.log("<all %d>", steps.size());
        var any = false;
        for (var step: steps) {
            any |= step.apply(state, services);
        }
        state.log("</all %s>", any);
        return any;
    }
}
