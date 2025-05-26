package code.kata.kata16.engine;

import code.kata.kata16.*;
import code.kata.kata17.OrderWorkflowKey;

public record IsStarted(OrderWorkflowKey key) implements Condition<OrderProcessingState> {

    @Override
    public boolean test(OrderProcessingState state, OtherServices services) {
        return state.isStarted(key);
    }
}
