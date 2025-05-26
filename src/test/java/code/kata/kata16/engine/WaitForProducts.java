package code.kata.kata16.engine;

import code.kata.kata16.*;

public record WaitForProducts(boolean value) implements Action<OrderProcessingState> {

    public WaitForProducts() {
        this(true);
    }

    public WaitForProducts {
        if (!value) throw new IllegalArgumentException();
    }

    @Override
    public boolean apply(OrderProcessingState state, OtherServices services) {
        state.log("wait for products: %s", state.id());
        services.waitForProducts(state.id());
        return true;
    }
}
