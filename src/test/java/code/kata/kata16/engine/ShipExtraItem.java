package code.kata.kata16.engine;

import code.kata.kata16.OtherServices;
import code.kata.kata16.ProductType;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public record ShipExtraItem(String name, ProductType type) implements Action<OrderProcessingState> {

    @Override
    public boolean apply(OrderProcessingState state, OtherServices services) {
        var item = state.order().extraItem(name, type);
        state.log("Adding extra %s '%s'", type, item);
        state.packingSlip().items().add(item);
        return true;
    }
}
