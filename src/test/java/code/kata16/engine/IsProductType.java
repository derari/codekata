package code.kata16.engine;

import code.kata16.OtherServices;
import code.kata16.ProductType;

public record IsProductType(ProductType type) implements Condition<OrderProcessingState> {

    @Override
    public boolean test(OrderProcessingState state, OtherServices services) {
        return state.order().productType().is(type);
    }
}
