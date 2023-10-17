package code.kata16.engine;

import code.kata16.OtherServices;

public record HasSalesAgent() implements Condition<PaymentProcessingState> {

    @Override
    public boolean test(PaymentProcessingState state, OtherServices services) {
        return state.order().salesAgentId() != null;
    }
}
