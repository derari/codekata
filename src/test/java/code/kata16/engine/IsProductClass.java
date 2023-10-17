package code.kata16.engine;

import code.kata16.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public record IsProductClass(
        @JsonProperty(value = "class", required = true)
        ProductClass clazz
) implements Condition<PaymentProcessingState> {

    @Override
    public boolean test(PaymentProcessingState state, OtherServices services) {
        return state.order().productType().getProductClass() == clazz;
    }
}
