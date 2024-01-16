package code.kata16.engine;

import code.kata16.OtherServices;

public record HasSalesAgent() implements Condition<OrderProcessingState> {

    @Override
    public boolean test(OrderProcessingState state, OtherServices services) {
        return state.order().salesAgentId() != null;
    }
}
