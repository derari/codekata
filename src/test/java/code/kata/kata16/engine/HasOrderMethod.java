package code.kata.kata16.engine;

import code.kata.kata16.OtherServices;
import code.kata.kata17.*;

public record HasOrderMethod(OrderMethod method) implements Condition<OrderProcessingState> {

    @Override
    public boolean test(OrderProcessingState state, OtherServices services) {
        return method == state.order().payment().getOrderMethod();
    }
}
