package code.kata16.engine;

import code.kata16.OtherServices;

public record HasProductName(String name) implements Condition<OrderProcessingState> {

    @Override
    public boolean test(OrderProcessingState state, OtherServices services) {
        return state.order().productName().equals(name);
    }
}
