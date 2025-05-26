package code.kata.kata16.engine;

import code.kata.kata16.OtherServices;

public record UpgradeMembership() implements Action<OrderProcessingState> {

    @Override
    public boolean apply(OrderProcessingState state, OtherServices services) {
        state.log("Upgrading membership");
        services.upgradeMembership(state.order().customer(), state.order().productType());
        return true;
    }
}
