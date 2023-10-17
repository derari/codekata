package code.kata16.engine;

import code.kata16.OtherServices;

import java.util.List;

public record NoOp<T extends State>(boolean status) implements ProcessingRule<T> {

    public static final NoOp<PaymentProcessingState> NONE = new NoOp<>(false);
    public static final NoOp<PaymentProcessingState> EMPTY = new NoOp<>(true);

    public static <T extends State> ProcessingRule<T> nonNull(ProcessingRule<T> rule) {
        return rule != null ? rule : new NoOp<>(false);
    }

    @Override
    public boolean apply(T state, OtherServices services) {
        return status;
    }
}
