package code.kata16.engine;

import code.kata16.OtherServices;

public record ActivateMembership() implements ProcessingRule<PaymentProcessingState> {

    @Override
    public boolean apply(PaymentProcessingState state, OtherServices services) {
        state.log("Activating membership");
        services.activateMembership(state.order().customer(), state.order().productType());
        return true;
    }
}
