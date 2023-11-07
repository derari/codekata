package code.kata16.engine;

import code.kata16.OtherServices;
import code.kata16.ProductType;

public record IsProductType(ProductType type) implements Condition<PaymentProcessingState> {

    @Override
    public boolean test(PaymentProcessingState state, OtherServices services) {
        return state.order().productType().is(type);
    }
}
