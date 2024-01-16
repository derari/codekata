package code.kata16.engine;

import code.kata16.OtherServices;

public record SubmitPackingSlips() implements Action<OrderProcessingState> {

    @Override
    public boolean apply(OrderProcessingState state, OtherServices services) {
        state.packingSlipDestinations().forEach(department -> {
            state.log("Submitting packing slip to %s", department);
            services.generatePackingSlip(department, state.packingSlip());
        });
        return !state.packingSlipDestinations().isEmpty();
    }
}
