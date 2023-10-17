package code.kata16.engine;

import code.kata16.OtherServices;

public record UpgradeMembership() implements ProcessingRule<PaymentProcessingState> {

    @Override
    public boolean apply(PaymentProcessingState state, OtherServices services) {
        state.log("Upgrading membership");
        services.upgradeMembership(state.order().customer(), state.order().productType());
        return true;
    }
}
