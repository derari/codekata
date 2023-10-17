package code.kata16.engine;

import code.kata16.OtherServices;
import code.kata16.ProductType;

public record ShipExtraItem(String name, ProductType type, String comment) implements ProcessingRule<PaymentProcessingState> {

    @Override
    public boolean apply(PaymentProcessingState state, OtherServices services) {
        var item = state.order().extraItem(name, type);
        state.log("Packing free '%s' (%s)", item, comment);
        state.packingSlip().items().add(item);
        return true;
    }
}
