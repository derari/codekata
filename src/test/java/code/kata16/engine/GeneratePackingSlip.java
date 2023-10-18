package code.kata16.engine;

import code.kata16.Department;
import code.kata16.OtherServices;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GeneratePackingSlip(
        @JsonProperty(required = true)
        Department department
) implements Action<PaymentProcessingState> {

    @Override
    public boolean apply(PaymentProcessingState state, OtherServices services) {
        state.log("Adding packing slip destination %s", department);
        state.packingSlipDestinations().add(department());
        return true;
    }
}
