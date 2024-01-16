package code.kata16.engine;

import code.kata16.OtherServices;
import code.kata17.*;

public record HasOrderMethod(OrderMethod method) implements Condition<OrderProcessingState> {

    @Override
    public boolean test(OrderProcessingState state, OtherServices services) {
        return method == state.order().payment().getOrderMethod();
    }
}
