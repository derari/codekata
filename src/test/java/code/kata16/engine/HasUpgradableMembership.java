package code.kata16.engine;

import code.kata16.OtherServices;

public record HasUpgradableMembership() implements Condition<PaymentProcessingState> {

    @Override
    public boolean test(PaymentProcessingState state, OtherServices services) {
        return state.order().productType().getUpgradable().stream()
                .anyMatch(m -> services.hasMembership(state.order().customer(), m));
    }
}
