package code.kata.kata16.engine;

import code.kata.kata16.OtherServices;

public record ActivateMembership() implements Action<OrderProcessingState> {

    @Override
    public boolean apply(OrderProcessingState state, OtherServices services) {
        state.log("Activating membership");
        services.activateMembership(state.order().customer(), state.order().productType());
        return true;
    }
}
