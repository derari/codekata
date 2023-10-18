package code.kata16.engine;

import code.kata16.OtherServices;

public record GenerateCommission() implements Action<PaymentProcessingState> {

    @Override
    public boolean apply(PaymentProcessingState state, OtherServices services) {
        state.log("Generating commission payment for %s", state.order());
        services.generateCommissionPayment(state.order());
        return true;
    }
}
