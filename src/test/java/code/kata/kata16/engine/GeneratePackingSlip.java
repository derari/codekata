package code.kata.kata16.engine;

import code.kata.kata16.Department;
import code.kata.kata16.OtherServices;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GeneratePackingSlip(
        @JsonProperty(required = true)
        Department department
) implements Action<OrderProcessingState> {

    @Override
    public boolean apply(OrderProcessingState state, OtherServices services) {
        state.log("Adding packing slip destination %s", department);
        state.packingSlipDestinations().add(department());
        return true;
    }
}
