package code.kata.kata16.engine;

import code.kata.kata16.OtherServices;

public record NoOp<T extends State>(boolean status) implements Action<T> {

    public static final NoOp<OrderProcessingState> NONE = new NoOp<>(false);
    public static final NoOp<OrderProcessingState> EMPTY = new NoOp<>(true);

    public static <T extends State> Action<T> nonNull(Action<T> rule) {
        return rule != null ? rule : new NoOp<>(false);
    }

    @Override
    public boolean apply(T state, OtherServices services) {
        return status;
    }
}
