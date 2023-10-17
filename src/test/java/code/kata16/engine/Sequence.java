package code.kata16.engine;

import code.kata16.OtherServices;

import java.util.List;

public record Sequence<T extends State>(List<ProcessingRule<T>> steps) implements ProcessingRule<T> {

    public static <T extends State> ProcessingRule<T> of(List<ProcessingRule<T>> steps) {
        if (steps == null) return new NoOp<>(false);
        if (steps.isEmpty()) return new NoOp<>(true);
        if (steps.size() == 1) return steps.get(0);
        return new Sequence<>(steps);
    }

    @SafeVarargs
    public Sequence(ProcessingRule<T>... steps) {
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
