package code.kata16.engine;

import code.kata16.*;

import java.util.*;
import java.util.function.BiConsumer;

public record PaymentProcessingState(
        BiConsumer<String, Object[]> logger,
        Order order,
        PackingSlip packingSlip,
        Set<Department> packingSlipDestinations
) implements State {

    public PaymentProcessingState(BiConsumer<String, Object[]> logger, Order order) {
        this(logger, order, new PackingSlip(order), new LinkedHashSet<>());
    }

    public void log(String message, Object... args) {
        logger().accept(message, args);
    }
}
